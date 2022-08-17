import UTILITIES from "./utilities.js";
import DOCUMENT from "./document.js";
import SETTINGS from "./settings.js";
import ACTIONS from "./actions.js";

const OBJECT_CACHE = {};
const DRAWING = {
	drawMap: (lineQueue, stationQueue) => {
		for (const key in OBJECT_CACHE) {
			if (!(key in lineQueue) && !(key in stationQueue)) {
				CANVAS.remove(OBJECT_CACHE[key]);
				delete OBJECT_CACHE[key];
			}
		}

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
					"color": `${UTILITIES.convertColor(Math.round(Math.random() * 0xFFFFFF))}50`,
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
				});
			}
		}
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

let mouseDown = false;
let shouldClearPanes = false;
let touchX = 0;
let touchY = 0;
let zoom = 1;
let startX = 0;
let startY = 0;
let targetX = undefined;
let targetY = undefined;

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
};
window.onresize = resize;
resize();

CANVAS.on("mouse:down", options => {
	mouseDown = true;
	shouldClearPanes = true;
	const event = options.e;
	if ("touches" in event && event.touches.length > 0) {
		touchX = event.touches[0].pageX;
		touchY = event.touches[0].pageY;
	}
});
CANVAS.on("mouse:up", () => {
	mouseDown = false;
	if (shouldClearPanes) {
		DOCUMENT.clearPanes(true);
	}
});
CANVAS.on("mouse:move", options => {
	const event = options.e;
	if (mouseDown) {
		if ("movementX" in event && "movementY" in event) {
			CANVAS.relativePan(new fabric.Point(event.movementX, event.movementY));
		} else if ("touches" in event && event.touches.length > 0) {
			CANVAS.relativePan(new fabric.Point(event.touches[0].pageX - touchX, event.touches[0].pageY - touchY));
			touchX = event.touches[0].pageX;
			touchY = event.touches[0].pageY;
		}
		shouldClearPanes = false;
	}
	event.stopPropagation();
});
CANVAS.on("mouse:wheel", options => {
	const event = options.e;
	DRAWING.zoom(event.deltaY, event.offsetX, event.offsetY);
	event.preventDefault();
	event.stopPropagation();
});

const inBounds = (x, y) => UTILITIES.isBetween(x, -getCanvasOffsetX(), window.innerWidth - getCanvasOffsetX()) && UTILITIES.isBetween(y, -getCanvasOffsetY(), window.innerHeight - getCanvasOffsetY());
const inWindow = (x1, y1, x2, y2) => UTILITIES.isBetween(-getCanvasOffsetX(), x1, x2) || UTILITIES.isBetween(window.innerWidth - getCanvasOffsetX(), x1, x2) || UTILITIES.isBetween(-getCanvasOffsetY(), y1, y2) || UTILITIES.isBetween(window.innerHeight - getCanvasOffsetY(), y1, y2);
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
		element.on("mousedown", () => {
			ACTIONS.onClickStation(id);
			shouldClearPanes = false;
		});
	});
	CANVAS.remove(OBJECT_CACHE[key]);
	OBJECT_CACHE[key] = group;
	group.checkVisibility();
}
const addLine = (key, line) => {
	const {color, segments, selected} = line;
	const polyline = new fabric.Polyline([], {
		"fill": null,
		"stroke": selected ? color : UTILITIES.convertColor(UTILITIES.getColorStyle("--textColorDisabled")),
		"strokeWidth": SETTINGS.size * 6,
		"cornerStyle": "circle",
		"objectCaching": false,
		"hoverCursor": "pointer",
		"selectable": false,
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
		},
		"z": selected ? 3 : 1,
	});
	CANVAS.remove(OBJECT_CACHE[key]);
	OBJECT_CACHE[key] = polyline;
	polyline.checkVisibility();
};

const checkAllVisibility = () => {
	const tempObjectCache = {...OBJECT_CACHE};
	UTILITIES.runForTime(tempObjectCache, key => {
		if (tempObjectCache[key] && key in OBJECT_CACHE) {
			OBJECT_CACHE[key].checkVisibility();
		}
		tempObjectCache[key] = undefined;
	}, () => setTimeout(checkAllVisibility));
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
	if (SETTINGS.selectedRoutes.length > 0) {
		CANVAS._objects.sort((a, b) => Math.sign(a["z"] - b["z"]));
	}
	CANVAS.renderAll();
};
update();

export default DRAWING;
