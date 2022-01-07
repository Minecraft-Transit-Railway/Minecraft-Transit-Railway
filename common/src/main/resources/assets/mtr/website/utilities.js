import SETTINGS from "./index.js"

const SCALE_UPPER_LIMIT = 64;
const SCALE_LOWER_LIMIT = 1 / 128;
const DRAG_THRESHOLD = 10;
const STEPS = 50;
const MIN_SPEED = 0.0001;
const SPEED_MULTIPLIER = 3;

const isBetween = (x, a, b) => x >= Math.min(a, b) && x <= Math.max(a, b);

let scale = 1;
let dragging = false;
let dragCounter = 0;
let mouseClickX = 0;
let mouseClickY = 0;

let step = 0;
let startX = 0;
let startY = 0;
let targetX = undefined;
let targetY = undefined;

let textCache = {};

const CANVAS = {
	convertX: x => Math.floor((x + window.innerWidth / 2) * scale / SETTINGS.lineSize) * SETTINGS.lineSize,
	convertY: y => Math.floor((y + window.innerHeight / 2) * scale / SETTINGS.lineSize) * SETTINGS.lineSize,
	onCanvasMouseDown: event => {
		dragging = true;
		mouseClickX = event.data.global.x;
		mouseClickY = event.data.global.y;
		targetX = undefined;
		targetY = undefined;
	},
	onCanvasMouseMove: function (event, container) {
		if (dragging) {
			const mouseX = event.data.global.x;
			const mouseY = event.data.global.y;

			if (isBetween(mouseX, 0, window.innerWidth) && isBetween(mouseY, 0, window.innerHeight)) {
				const changeX = mouseX - mouseClickX;
				const changeY = mouseY - mouseClickY;
				container.x += changeX;
				container.y += changeY;
				dragCounter += Math.abs(changeX) + Math.abs(changeY);
				mouseClickX = event.data.global.x;
				mouseClickY = event.data.global.y;
			} else {
				this.onCanvasMouseUp();
			}
		}

		return dragCounter > DRAG_THRESHOLD;
	},
	onCanvasMouseUp: () => {
		dragging = false;
		dragCounter = 0;
	},
	onCanvasScroll: function (event, container) {
		this.onZoom(Math.sign(event.deltaY), event.offsetX, event.offsetY, container);
	}, onZoom: (amount, offsetX, offsetY, container) => {
		scale *= amount < 0 ? 2 : 0.5;
		scale = Math.min(SCALE_UPPER_LIMIT, Math.max(SCALE_LOWER_LIMIT, scale));
		container.x -= Math.round((offsetX - container.x) / (amount < 0 ? 1 : -2));
		container.y -= Math.round((offsetY - container.y) / (amount < 0 ? 1 : -2));
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
	drawText: (textArray, text, icons, x, y) => {
		const textSplit = text.split("|");
		let yStart = y;
		for (const textPart of textSplit) {
			const isTextCJK = SETTINGS.isCJK(textPart);

			if (typeof textCache[textPart + x + "_" + y] === "undefined") {
				const richText = new PIXI.Text(textPart, {
					fontFamily: ["Noto Sans", "Noto Serif TC", "Noto Serif SC", "Noto Serif JP", "Noto Serif KR"],
					fontSize: (isTextCJK ? 3 : 1.5) * SETTINGS.lineSize,
					fill: SETTINGS.getColorStyle("--textColor"),
					stroke: SETTINGS.getColorStyle("--backgroundColor"),
					strokeThickness: 2,
				});
				richText.anchor.set(0.5, 0);
				textCache[textPart + x + "_" + y] = richText;
			}

			const richText = textCache[textPart + x + "_" + y];
			richText.position.set(Math.round(x / 2) * 2, yStart);
			textArray.push(richText);
			yStart += (isTextCJK ? 3 : 1.5) * SETTINGS.lineSize;
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
};

export default CANVAS;
