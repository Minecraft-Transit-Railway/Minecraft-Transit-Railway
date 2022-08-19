import UTILITIES from "./utilities.js";
import DOCUMENT from "./document.js";
import SETTINGS from "./settings.js";
import ACTIONS from "./actions.js";
import DATA from "./data.js";

const OBJECT_CACHE = {};
const DRAWING = {
	drawMap: (lineQueue, stationQueue) => {
		for (const key in OBJECT_CACHE) {
			if (!(key in lineQueue) && !(key in stationQueue)) {
				CANVAS.remove(OBJECT_CACHE[key]);
				delete OBJECT_CACHE[key];
			}
		}
		const cacheObjects = Object.values(OBJECT_CACHE);
		CANVAS.getObjects().forEach(object => {
			if (!cacheObjects.includes(object)) {
				CANVAS.remove(object);
			}
		});

		const addLineFromQueue = key => {
			addLine(key, lineQueue[key]);
			delete lineQueue[key];
		};
		const addStationFromQueue = key => {
			addStation(key, stationQueue[key]);
			delete stationQueue[key];
		};

		setTimeout(() => UTILITIES.runForTime(stationQueue, addStationFromQueue, setTimeout(() => UTILITIES.runForTime(lineQueue, addLineFromQueue, null))));
		document.getElementById("loading").style.display = "none";
		CANVAS.backgroundColor = UTILITIES.convertColor(UTILITIES.getColorStyle("--backgroundColor"));
	},
	drawTest: (routes, middleAngle, outerAngles) => {
		CANVAS.clear();
		addStation("Middle", {
			"width": SETTINGS.size * 6 * (routes + 1),
			"height": SETTINGS.size * 12,
			"left": 0,
			"top": 0,
			"angle": middleAngle,
		});
		for (let i = 0; i < 8; i++) {
			const stationX = 400 * Math.cos(2 * Math.PI * (i + 0.5) / 8);
			const stationY = 400 * Math.sin(2 * Math.PI * (i + 0.5) / 8);
			addStation(`Outer ${i}`, {
				"width": SETTINGS.size * 6 * (routes + 1),
				"height": SETTINGS.size * 12,
				"left": stationX,
				"top": stationY,
				"angle": outerAngles,
			});
			for (let j = 0; j < routes; j++) {
				addLine(`test_line_${i}_${j}`, {
					"color": Math.round(Math.random() * 0xFFFFFF),
					"segments": [
						{
							"x": 0,
							"y": 0,
							"direction": middleAngle,
							"offsetIndex": j,
							"routeCount": routes,
						},
						{
							"x": stationX,
							"y": stationY,
							"direction": outerAngles,
							"offsetIndex": j,
							"routeCount": routes,
						},
					],
					"selected": true,
				});
			}
		}
		document.getElementById("loading").style.display = "none";
	},
	zoomToPoint: (x, y) => {
		targetX = x * zoom - window.innerWidth / 2;
		targetY = y * zoom - window.innerHeight / 2;
		startX = -getCanvasOffsetX();
		startY = -getCanvasOffsetY();
	},
	zoom: (amount, x, y) => {
		const zoomFactor = 0.999 ** amount;
		zoom *= zoomFactor;
		Object.values(OBJECT_CACHE).forEach(object => {
			if ("updateShape" in object) {
				object.updateShape();
			} else {
				object.left *= zoomFactor;
				if ("topOffset" in object) {
					object.top = (object.top - object.topOffset) * zoomFactor + object.topOffset;
				} else {
					object.top *= zoomFactor;
				}
			}
		});
		const offsetX = x - getCanvasOffsetX();
		const offsetY = y - getCanvasOffsetY();
		CANVAS.relativePan(new fabric.Point(offsetX - offsetX * zoomFactor, offsetY - offsetY * zoomFactor));
	},
	getCenterLatLon: () => [(getCanvasOffsetY() - window.innerHeight / 2) / zoom, -(getCanvasOffsetX() - window.innerWidth / 2) / zoom, window.innerHeight / zoom, window.innerWidth / zoom],
};

let mouseDown = 0;
let shouldClearPanes = false;
let touchX = 0;
let touchY = 0;
let fingerCount = 0;
let twoFingerDistance = 0;
let zoom = 1;
let startX = 0;
let startY = 0;
let targetX = undefined;
let targetY = undefined;
let legendVisibleLines = [];
let previousLegendString = "";
let hoveredLines = [];

const CANVAS = new fabric.Canvas("canvas", {
	renderOnAddRemove: false,
	selection: false,
	backgroundColor: UTILITIES.convertColor(UTILITIES.getColorStyle("--backgroundColor")),
});
CANVAS.absolutePan(new fabric.Point(-window.innerWidth / 2, -window.innerHeight / 2));
const getCanvasOffsetX = () => CANVAS.viewportTransform[4];
const getCanvasOffsetY = () => CANVAS.viewportTransform[5];
const resize = () => {
	CANVAS.setWidth(window.innerWidth);
	CANVAS.setHeight(window.innerHeight);
	document.getElementById("search").style.maxWidth = window.innerWidth - 32 + "px";
	document.getElementById("station_info").style.maxHeight = window.innerHeight - 80 + "px";
	const legendElement = document.getElementById("legend");
	legendElement.style.maxWidth = window.innerWidth - 432 + "px";
	legendElement.style.maxHeight = window.innerHeight - 32 + "px";
};
window.onresize = resize;
resize();

CANVAS.on("mouse:down", options => {
	mouseDown = Date.now();
	shouldClearPanes = true;
	twoFingerDistance = 0;
	const event = options.e;
	if ("touches" in event) {
		const newFingerCount = event.touches.length;
		let newTouchX = 0;
		let newTouchY = 0;
		for (let i = 0; i < newFingerCount; i++) {
			newTouchX += event.touches[i].pageX;
			newTouchY += event.touches[i].pageY;
		}
		touchX = newTouchX / newFingerCount;
		touchY = newTouchY / newFingerCount;
	}
});
CANVAS.on("mouse:up", options => {
	const event = options.e;
	if (!("touches" in event) || event.touches.length === 0) {
		mouseDown = 0;
	}
	if (shouldClearPanes) {
		DOCUMENT.clearPanes(true);
		DATA.redraw();
	}
});
CANVAS.on("mouse:move", options => {
	const event = options.e;
	if (mouseDown > 0) {
		if ("movementX" in event && "movementY" in event) {
			CANVAS.relativePan(new fabric.Point(event.movementX, event.movementY));
			shouldClearPanes = false;
		} else if ("touches" in event) {
			const newFingerCount = event.touches.length;

			if (newFingerCount > 0) {
				let newTouchX = 0;
				let newTouchY = 0;
				for (let i = 0; i < newFingerCount; i++) {
					newTouchX += event.touches[i].pageX;
					newTouchY += event.touches[i].pageY;
				}
				newTouchX /= newFingerCount;
				newTouchY /= newFingerCount;
				const newTwoFingerDistance = newFingerCount > 1 ? distanceSquared(event.touches[0].pageX, event.touches[0].pageY, event.touches[1].pageX, event.touches[1].pageY) : 0;

				if (newFingerCount === fingerCount) {
					if (twoFingerDistance > 0) {
						DRAWING.zoom((twoFingerDistance - newTwoFingerDistance) / 50, newTouchX, newTouchY);
					}
					CANVAS.relativePan(new fabric.Point(newTouchX - touchX, newTouchY - touchY));
				}

				touchX = newTouchX;
				touchY = newTouchY;
				twoFingerDistance = newTwoFingerDistance;
			}

			fingerCount = newFingerCount;
			if (Date.now() - mouseDown > 100) {
				shouldClearPanes = false;
			}
		}
	}
	// getSegment(event.offsetX, event.offsetY); // TODO mouse detection
	event.stopPropagation();
});
CANVAS.on("mouse:wheel", options => {
	const event = options.e;
	DRAWING.zoom(event.deltaY, event.offsetX, event.offsetY);
	event.preventDefault();
	event.stopPropagation();
});

const distanceSquared = (x1, y1, x2, y2) => (Math.abs(x2 - x1) ** 2) + (Math.abs(y2 - y1) ** 2);
const inBounds = (x, y) => UTILITIES.isBetween(x, -getCanvasOffsetX(), window.innerWidth - getCanvasOffsetX()) && UTILITIES.isBetween(y, -getCanvasOffsetY(), window.innerHeight - getCanvasOffsetY());
const inWindow = (x1, y1, x2, y2) => {
	const minX = Math.min(x1, x2);
	const minY = Math.min(y1, y2);
	const maxX = Math.max(x1, x2);
	const maxY = Math.max(y1, y2);
	return minX < window.innerWidth - getCanvasOffsetX() && maxX > -getCanvasOffsetX() && minY < window.innerHeight - getCanvasOffsetY() && maxY > -getCanvasOffsetY();
};
const addStation = (key, station) => {
	const {id, width, height, left, top, angle, selected, types} = station;
	const blobHeightOffset = (angle === 0 ? height : (width + height - 1) / Math.SQRT2) / 2;
	const blob = new fabric.Rect({
		"originX": "center",
		"originY": "center",
		"width": width,
		"height": height,
		"rx": SETTINGS.size * 6,
		"ry": SETTINGS.size * 6,
		"angle": angle,
		"fill": UTILITIES.convertColor(UTILITIES.getColorStyle("--backgroundColor")),
		"stroke": UTILITIES.convertColor(UTILITIES.getColorStyle(selected ? "--textColor" : "--textColorDisabled")),
		"strokeWidth": SETTINGS.size * 2,
		"hoverCursor": "pointer",
		"selectable": false,
	});
	const elements = [blob];
	if (selected && SETTINGS.showText) {
		const nameSplit = key.split("|");
		let textYOffset = 0;
		for (let i = 0; i < nameSplit.length; i++) {
			const text = nameSplit[i];
			const fontSize = SETTINGS.size * (UTILITIES.isCJK(text) ? 18 : 9);
			elements.push(new fabric.Text(text, {
				"top": blobHeightOffset + SETTINGS.size * 2 + textYOffset,
				"fontFamily": UTILITIES.fonts.join(","),
				"fontSize": fontSize,
				"fill": UTILITIES.convertColor(UTILITIES.getColorStyle("--textColor")),
				"stroke": UTILITIES.convertColor(UTILITIES.getColorStyle("--backgroundColor")),
				"strokeWidth": 4,
				"paintFirst": "stroke",
				"originX": "center",
				"originY": "top",
				"hoverCursor": "pointer",
				"selectable": false,
			}));
			textYOffset += fontSize;
		}
		let routeTypeText = "";
		let routeTypeCount = 0;
		Object.keys(UTILITIES.routeTypes).forEach(routeType => {
			if (!SETTINGS.selectedRouteTypes.includes(routeType) && types.includes(routeType)) {
				routeTypeText += UTILITIES.routeTypes[routeType];
				routeTypeCount++;
			}
		});
		if (routeTypeText) {
			const element = new fabric.Text(routeTypeText, {
				"top": blobHeightOffset + SETTINGS.size * 2 + textYOffset + 4,
				"fontFamily": "Material Icons",
				"fontSize": SETTINGS.size * 18,
				"fill": UTILITIES.convertColor(UTILITIES.getColorStyle("--textColor")),
				"stroke": UTILITIES.convertColor(UTILITIES.getColorStyle("--backgroundColor")),
				"strokeWidth": 4,
				"paintFirst": "stroke",
				"originX": "center",
				"originY": "top",
				"hoverCursor": "pointer",
				"selectable": false,
			});
			elements.push(element);
			const context = document.createElement("canvas").getContext("2d");
			context.font = `${SETTINGS.size * 18}px Material Icons`;
			element.set({"width": context.measureText(routeTypeText).width});
		}
	}
	const group = new fabric.Group(elements, {
		"left": left * zoom,
		"top": top * zoom - blobHeightOffset - SETTINGS.size,
		"topOffset": -blobHeightOffset - SETTINGS.size,
		"originX": "center",
		"originY": "top",
		"subTargetCheck": true,
		"hoverCursor": "default",
		"selectable": false,
		"checkVisibility": () => {
			const shouldShow = inBounds(left * zoom, top * zoom);
			if (CANVAS.getObjects().includes(group)) {
				if (!shouldShow) {
					CANVAS.remove(group);
				}
			} else {
				if (shouldShow) {
					CANVAS.add(group);
				}
			}
		},
		"z": selected ? 4 : 2,
	});
	elements.forEach(element => {
		element.on("mouseover", () => group.set("shadow", `${UTILITIES.convertColor(UTILITIES.getColorStyle("--textColor"))} 0 0 8px`));
		element.on("mouseout", () => group.set("shadow", ""));
		element.on("mouseup", () => {
			if (!mouseDown && shouldClearPanes) {
				ACTIONS.onClickStation(id);
				shouldClearPanes = false;
			}
		});
	});
	CANVAS.remove(OBJECT_CACHE[key]);
	OBJECT_CACHE[key] = group;
	group.checkVisibility();
}
const addLine = (key, line) => {
	const {color, segments, selected, density, id} = line;

	const grayColor = UTILITIES.getColorStyle("--textColorDisabled");
	let newColor;
	if (SETTINGS.densityView === 1) {
		const grayByte = (grayColor & 0xFF) * (1 - density);
		const r = Math.floor(((color >> 16) & 0xFF) * density + grayByte);
		const g = Math.floor(((color >> 8) & 0xFF) * density + grayByte);
		const b = Math.floor((color & 0xFF) * density + grayByte);
		newColor = (r << 16) + (g << 8) + b;
	} else if (SETTINGS.densityView === 2) {
		if (density > 0) {
			newColor = (Math.floor(0x66 * (1 - density)) << 16) + (Math.floor(0xCC * density) << 8) + 0x3300;
		} else {
			newColor = grayColor;
		}
	} else {
		newColor = color;
	}

	const polyline = new fabric.Polyline([], {
		"fill": null,
		"stroke": `${UTILITIES.convertColor(selected ? newColor : grayColor)}${UTILITIES.testMode ? "50" : ""}`,
		"strokeWidth": SETTINGS.size * 6,
		"strokeLineCap": "round",
		"strokeLineJoin": "round",
		"objectCaching": false,
		"hoverCursor": "pointer",
		"selectable": false,
		"routeId": id,
		"updateShape": () => {
			const point1 = segments[0];
			const point2 = segments[1];
			const point1X = point1["x"] * zoom;
			const point1Y = point1["y"] * zoom;
			const point2X = point2["x"] * zoom;
			const point2Y = point2["y"] * zoom;
			const newSegments = [];
			UTILITIES.connectLine(point1X, point1Y, point1["direction"], point1["offsetIndex"], point1["routeCount"], point2X, point2Y, point2["direction"], point2["offsetIndex"], point2["routeCount"], SETTINGS.size * 6, newSegments);
			polyline.points = newSegments;
			return inBounds(point1X, point1Y) || inBounds(point2X, point2Y) || inWindow(point1X, point1Y, point2X, point2Y);
		},
		"checkVisibility": () => {
			const shouldShow = polyline.updateShape();
			if (CANVAS.getObjects().includes(polyline)) {
				if (!shouldShow) {
					CANVAS.remove(polyline);
				}
			} else {
				if (shouldShow) {
					CANVAS.sendToBack(polyline);
				}
			}
			if (shouldShow && !legendVisibleLines.includes(id)) {
				legendVisibleLines.push(id);
			}
			// polyline["stroke"] = UTILITIES.convertColor(hoveredLines.includes(id) ? UTILITIES.getColorStyle("--textColorDisabled") : selected ? newColor : grayColor);
		},
		"z": selected ? 3 : 1,
	});
	CANVAS.remove(OBJECT_CACHE[key]);
	OBJECT_CACHE[key] = polyline;
	polyline.checkVisibility();
};
const getSegment = (x, y) => {
	hoveredLines = [];
	CANVAS.getObjects().forEach(object => {
		if ("points" in object) {
			const segments = object["points"];
			for (let i = 1; i < segments.length; i++) {
				const point1 = segments[i - 1];
				const point2 = segments[i];
				let x1 = point1["x"] + getCanvasOffsetX();
				let y1 = point1["y"] + getCanvasOffsetY();
				let x2 = point2["x"] + getCanvasOffsetX();
				let y2 = point2["y"] + getCanvasOffsetY();
				let mouseX = x;
				let mouseY = y;
				let minX = Math.min(x1, x2);
				let minY = Math.min(y1, y2);
				let maxX = Math.max(x1, x2);
				let maxY = Math.max(y1, y2);

				const checkBounds = () => UTILITIES.isBetween(mouseX, minX - SETTINGS.size * 3, maxX + SETTINGS.size * 3) && UTILITIES.isBetween(mouseY, minY - SETTINGS.size * 3, maxY + SETTINGS.size * 3);
				if (checkBounds()) {
					if (x1 !== x2 && y1 !== y2) {
						const rotatedPoint1 = UTILITIES.rotatePoint(x1, y1, 45);
						x1 = rotatedPoint1["x"];
						y1 = rotatedPoint1["y"];
						const rotatedPoint2 = UTILITIES.rotatePoint(x2, y2, 45);
						x2 = rotatedPoint2["x"];
						y2 = rotatedPoint2["y"];
						const rotatedPoint3 = UTILITIES.rotatePoint(mouseX, mouseY, 45);
						mouseX = rotatedPoint3["x"];
						mouseY = rotatedPoint3["y"];

						minX = Math.min(x1, x2);
						minY = Math.min(y1, y2);
						maxX = Math.max(x1, x2);
						maxY = Math.max(y1, y2);

						if (checkBounds()) {
							hoveredLines.push(object["routeId"]);
						}
					} else {
						hoveredLines.push(object["routeId"]);
					}
				}
			}
		}
	});
};

const checkAllVisibility = () => {
	const tempObjectCache = {...OBJECT_CACHE};
	UTILITIES.runForTime(tempObjectCache, key => {
		if (tempObjectCache[key] && key in OBJECT_CACHE) {
			OBJECT_CACHE[key].checkVisibility();
		}
		tempObjectCache[key] = undefined;
	}, () => setTimeout(checkAllVisibility));

	legendVisibleLines.sort();
	const legendString = legendVisibleLines.join(",") + SETTINGS.selectedRoutes.join(",") + Object.keys(SETTINGS.selectedDirectionsSegments).join(",");
	const legendElement = document.getElementById("legend");
	if (legendString !== previousLegendString) {
		legendElement.innerText = "";
		legendVisibleLines.forEach(routeId => {
			const route = DATA.json[SETTINGS.dimension]["routes"].find(route => route["id"] === routeId);
			if (route) {
				legendElement.appendChild(ACTIONS.getRouteElement(routeId, route["color"], route["name"], route["number"], route["type"], true, DATA.routeSelected(routeId), "", false));
			}
		});
	}
	previousLegendString = legendString;
	legendElement.style.display = !SETTINGS.showLegend || legendVisibleLines.length === 0 || window.innerWidth < 480 ? "none" : "";
	legendVisibleLines = [];
};
checkAllVisibility();

const STEPS = 50;
const MIN_SPEED = 0.0001;
const SPEED_MULTIPLIER = 3;
const delta = 1;
const update = () => {
	requestAnimationFrame(update);
	if (typeof targetX !== "undefined" && typeof targetY !== "undefined") {
		const distanceSquared = (targetX - startX) ** 2 + (targetY - startY) ** 2;
		const percentage = distanceSquared === 0 ? 1 : Math.sqrt(((getCanvasOffsetX() + startX) ** 2 + (getCanvasOffsetY() + startY) ** 2) / distanceSquared);
		const speed = delta * SPEED_MULTIPLIER * Math.sqrt(percentage < 0.5 ? Math.max(percentage, MIN_SPEED) : 1 - percentage);
		CANVAS.relativePan(new fabric.Point(-speed * (targetX - startX) / STEPS, -speed * (targetY - startY) / STEPS));
		if (percentage >= 1 || (-getCanvasOffsetX() - startX) / (targetX - startX) >= 1 || (-getCanvasOffsetY() - startY) / (targetY - startY) >= 1) {
			CANVAS.absolutePan(new fabric.Point(targetX, targetY));
			targetX = undefined;
			targetY = undefined;
		}
	}
	if (SETTINGS.selectedRoutes.length > 0 || SETTINGS.selectedDirectionsStations.length > 0) {
		CANVAS._objects.sort((a, b) => Math.sign(a["z"] - b["z"]));
	}
	CANVAS.renderAll();
};
update();

export default DRAWING;
