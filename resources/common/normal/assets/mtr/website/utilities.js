import SETTINGS from "./index.js";
import drawMap from "./drawing.js";

const SCALE_UPPER_LIMIT = 64;
const SCALE_LOWER_LIMIT = 1 / 128;
const SCROLL_CALLBACK_DELAY = 100;
const STEPS = 50;
const MIN_SPEED = 0.0001;
const SPEED_MULTIPLIER = 3;

let scale = 1;
let scaleStart = 1;
let previousPinch = 0;
let zoomNotStarted = true;
let scrollTimeoutId = 0;
let step = 0;
let startX = 0;
let startY = 0;
let targetX = undefined;
let targetY = undefined

let textCache = {};
let textCacheKeep = [];

const CANVAS = {
	convertX: x => Math.floor((x + window.innerWidth / 2) * scale / SETTINGS.lineSize) * SETTINGS.lineSize,
	convertY: y => Math.floor((y + window.innerHeight / 2) * scale / SETTINGS.lineSize) * SETTINGS.lineSize,
	onCanvasMouseMove: function (event, container) {
		container.x += event.deltaX;
		container.y += event.deltaY;
	},
	onPinch: function (fingers, offsetX, offsetY, container, data, delay) {
		const fingerDistance = (Math.abs(fingers.x - offsetX) + Math.abs(fingers.y - offsetY)) * 2;
		this.onZoom(zoomNotStarted ? 0 : (previousPinch - fingerDistance) / SETTINGS.smoothScrollScale, offsetX, offsetY, container, data, delay);
		previousPinch = fingerDistance;
	},
	onZoom: (amount, offsetX, offsetY, container, data, delay) => {
		if (zoomNotStarted) {
			scaleStart = scale;
			zoomNotStarted = false;
		}

		let prevScale = scale;
		scale = Math.pow(2, Math.log2(scale) - amount);
		scale = Math.min(SCALE_UPPER_LIMIT, Math.max(SCALE_LOWER_LIMIT, scale));
		if (delay) {
			container.scale.set(scale / scaleStart, scale / scaleStart);
		}
		container.x -= Math.round((offsetX - container.x) * (scale / prevScale - 1));
		container.y -= Math.round((offsetY - container.y) * (scale / prevScale - 1));

		clearTimeout(scrollTimeoutId);
		if (delay) {
			scrollTimeoutId = setTimeout(() => {
				container.scale.set(1, 1);
				zoomNotStarted = true;
				drawMap(container, data)
			}, SCROLL_CALLBACK_DELAY);
		} else {
			drawMap(container, data);
		}
	},
	slideTo: (container, x, y) => {
		startX = container.x;
		startY = container.y;
		targetX = Math.round(x);
		targetY = Math.round(y);
		step = 0;
	},
	update: (delta, container) => {
		if (typeof targetX !== "undefined" && typeof targetY !== "undefined") {
			const distanceSquared = (targetX - startX) ** 2 + (targetY - startY) ** 2;
			const percentage = distanceSquared === 0 ? 1 : Math.sqrt(((container.x - startX) ** 2 + (container.y - startY) ** 2) / distanceSquared);
			const speed = delta * SPEED_MULTIPLIER * Math.sqrt(percentage < 0.5 ? Math.max(percentage, MIN_SPEED) : 1 - percentage);
			container.x += speed * (targetX - startX) / STEPS;
			container.y += speed * (targetY - startY) / STEPS;
			if (percentage >= 1 || (container.x - startX) / (targetX - startX) >= 1 || (container.y - startY) / (targetY - startY) >= 1) {
				container.x = targetX;
				container.y = targetY;
				targetX = undefined;
				targetY = undefined;
			}
		}
	},
	drawText: (textArray, stationId, text, icons, x, y) => {
		const textSplit = text.split("|");
		let yStart = y;
		for (const textPart of textSplit) {
			const isTextCJK = SETTINGS.isCJK(textPart);
			const key = textPart + " " + stationId;

			if (typeof textCache[key] === "undefined") {
				textCache[key] = new PIXI.Text(textPart, {
					fontFamily: ["Noto Sans", "Noto Serif TC", "Noto Serif SC", "Noto Serif JP", "Noto Serif KR"],
					fontSize: (isTextCJK ? 3 : 1.5) * SETTINGS.lineSize,
					fill: SETTINGS.getColorStyle("--textColor"),
					stroke: SETTINGS.getColorStyle("--backgroundColor"),
					strokeThickness: 2,
				});
			}

			const richText = textCache[key];
			richText.anchor.set(0.5, 0);
			richText.position.set(Math.round(x / 2) * 2, yStart);
			textArray.push(richText);
			yStart += (isTextCJK ? 3 : 1.5) * SETTINGS.lineSize;

			if (!textCacheKeep.includes(key)) {
				textCache[key] = undefined;
				textCacheKeep.push(key);
			}
		}

		if (icons !== "") {
			const richText = new PIXI.Text(icons, {
				fontFamily: "Material Icons",
				fontSize: 3 * SETTINGS.lineSize,
				fill: SETTINGS.getColorStyle("--textColor"),
				stroke: SETTINGS.getColorStyle("--backgroundColor"),
				strokeThickness: 2,
			});
			richText.anchor.set(0.5, 0);
			richText.position.set(Math.round(x / 2) * 2, yStart + SETTINGS.lineSize);
			textArray.push(richText);
		}
	},
	clearTextCache: () => textCache = {},
	drawLine: (graphics, x1, y1, vertical1, x2, y2, vertical2) => {
		const differenceX = Math.abs(x2 - x1);
		const differenceY = Math.abs(y2 - y1);
		const points = [];
		points.push({x: x1, y: y1});
		if (differenceX > differenceY) {
			if (vertical1 && vertical2) {
				const midpoint = (y1 + y2) / 2;
				points.push({x: x1 + Math.sign(x2 - x1) * Math.abs(midpoint - y1), y: midpoint});
				points.push({x: x2 - Math.sign(x2 - x1) * Math.abs(midpoint - y1), y: midpoint});
			} else if (!vertical1 && !vertical2) {
				points.push({x: x1 + Math.sign(x2 - x1) * (differenceX - differenceY) / 2, y: y1});
				points.push({x: x2 - Math.sign(x2 - x1) * (differenceX - differenceY) / 2, y: y2});
			} else if (vertical1) {
				points.push({x: x1 + Math.sign(x2 - x1) * differenceY, y: y2});
			} else {
				points.push({x: x2 - Math.sign(x2 - x1) * differenceY, y: y1});
			}
		} else {
			if (vertical1 && vertical2) {
				points.push({x: x1, y: y1 + Math.sign(y2 - y1) * (differenceY - differenceX) / 2});
				points.push({x: x2, y: y2 - Math.sign(y2 - y1) * (differenceY - differenceX) / 2});
			} else if (!vertical1 && !vertical2) {
				const midpoint = (x1 + x2) / 2;
				points.push({x: midpoint, y: y1 + Math.sign(y2 - y1) * Math.abs(midpoint - x1)});
				points.push({x: midpoint, y: y2 - Math.sign(y2 - y1) * Math.abs(midpoint - x1)});
			} else if (vertical1) {
				points.push({x: x1, y: y2 - Math.sign(y2 - y1) * differenceX});
			} else {
				points.push({x: x2, y: y1 + Math.sign(y2 - y1) * differenceX});
			}
		}
		points.push({x: x2, y: y2});

		graphics.moveTo(x1, y1);

		const offset1 = SETTINGS.lineSize / 2;
		const offset2 = offset1 / Math.sqrt(2);

		let reverse = false;
		do {
			let i = 0;
			while (i < points.length - 1) {
				const thisPoint = points[reverse ? points.length - 1 - i : i];
				const nextPoint = points[reverse ? points.length - 2 - i : i + 1];
				const thisX = thisPoint["x"];
				const thisY = thisPoint["y"];
				const nextX = nextPoint["x"];
				const nextY = nextPoint["y"];

				const signX = Math.sign(nextY - thisY);
				const signY = Math.sign(thisX - nextX);
				const offsetX = Math.round(signX * (signX !== 0 && signY !== 0 ? offset2 : offset1));
				const offsetY = Math.round(signY * (signX !== 0 && signY !== 0 ? offset2 : offset1));

				graphics.lineTo(thisX + offsetX, thisY + offsetY);
				graphics.lineTo(nextX + offsetX, nextY + offsetY);

				i++;
			}
			reverse = !reverse;
		} while (reverse);
	},
	getDrawStationElement: (stationElement, color1, color2) => {
		const element = document.createElement("div");
		element.className = "route_station_name";
		if (color1 != null) {
			element.innerHTML = `<span class="route_segment bottom" style="background-color: ${CANVAS.convertColor(color1)}">&nbsp</span>`;
		}
		if (color2 != null) {
			element.innerHTML += `<span class="route_segment top" style="background-color: ${CANVAS.convertColor(color2)}">&nbsp</span>`;
		}
		element.innerHTML += `<span class="station_circle"></span>`;
		element.appendChild(stationElement);
		return element;
	},
	getDrawLineElement: (icon, innerElement, color) => {
		const element = document.createElement("div");
		element.className = "route_duration";
		element.innerHTML =
			`<span class="route_segment ${color == null ? "walk" : ""}" style="background-color: ${color == null ? 0 : CANVAS.convertColor(color)}">&nbsp</span>` +
			`<span class="material-icons small">${icon}</span>`;
		element.appendChild(innerElement);
		return element;
	},
	getClosestInterchangeOnRoute: (data, route, thisStation) => {
		const {routes, stations} = data;
		const routeStations = route["stations"];
		let passed = false;
		for (let i = 0; i < routeStations.length; i++) {
			const checkStation = routeStations[i].split("_")[0];
			if (passed && (routes.some(checkRoute => checkRoute["color"] !== route["color"] && checkRoute["stations"].some(checkRouteStation => checkRouteStation.split("_")[0] === checkStation)) || i === routeStations.length - 1)) {
				return stations[checkStation]["name"];
			}
			if (checkStation === thisStation) {
				passed = true;
			}
		}
	},
	convertColor: color => "#" + Number(color).toString(16).padStart(6, "0"),
	formatTime: time => {
		const hour = Math.floor(time / 3600);
		const minute = Math.floor(time / 60) % 60;
		const second = Math.floor(time) % 60;
		return (hour > 0 ? hour.toString() + ":" : "") + (hour > 0 ? minute.toString().padStart(2, "0") : minute.toString()) + ":" + second.toString().padStart(2, "0");
	},
};

export default CANVAS;
