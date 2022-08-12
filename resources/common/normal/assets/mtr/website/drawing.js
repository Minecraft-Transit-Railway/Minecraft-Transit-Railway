import UTILITIES from "./utilities.js";

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
			const {color, segments} = lineQueue[key];
			addLine(key, color, segments);
			delete lineQueue[key];
		};
		const addStationFromQueue = key => {
			addStation(key, stationQueue[key]);
			delete stationQueue[key];
		};

		setTimeout(() => UTILITIES.runForTime(stationQueue, addStationFromQueue, setTimeout(() => UTILITIES.runForTime(lineQueue, addLineFromQueue, null))));
		document.getElementById("loading").style.display = "none";
	},
	drawTest: (routes, middleAngle, outerAngles) => {
		CANVAS.clear();
		addStation("Middle", {
			"width": UTILITIES.size * 6 * (routes + 1),
			"height": UTILITIES.size * 12,
			"left": 0,
			"top": 0,
			"angle": middleAngle,
		});
		for (let i = 0; i < 8; i++) {
			const stationX = 400 * Math.cos(2 * Math.PI * (i + 0.5) / 8);
			const stationY = 400 * Math.sin(2 * Math.PI * (i + 0.5) / 8);
			addStation(`Outer ${i}`, {
				"width": UTILITIES.size * 6 * (routes + 1),
				"height": UTILITIES.size * 12,
				"left": stationX,
				"top": stationY,
				"angle": outerAngles,
			});
			for (let j = 0; j < routes; j++) {
				addLine(`test_line_${i}_${j}`, `#${UTILITIES.convertColor(Math.round(Math.random() * 0xFFFFFF))}50`, [
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
				]);
			}
		}
	},
	getCenterLatLon: () => [(getCanvasOffsetY() - window.innerHeight / 2) / zoom, -(getCanvasOffsetX() - window.innerWidth / 2) / zoom, window.innerHeight / zoom, window.innerWidth / zoom],
};

let mouseDown = false;
let touchX = 0;
let touchY = 0;
let zoom = 1;

const CANVAS = new fabric.Canvas("canvas", {renderOnAddRemove: false, selection: false});
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
	const event = options.e;
	if ("touches" in event && event.touches.length > 0) {
		touchX = event.touches[0].pageX;
		touchY = event.touches[0].pageY;
	}
});
CANVAS.on("mouse:up", () => mouseDown = false);
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
	}
	event.stopPropagation();
});
CANVAS.on("mouse:wheel", options => {
	const event = options.e;
	const zoomFactor = 0.999 ** event.deltaY;
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
	const offsetX = event.offsetX - getCanvasOffsetX();
	const offsetY = event.offsetY - getCanvasOffsetY();
	CANVAS.relativePan(new fabric.Point(offsetX - offsetX * zoomFactor, offsetY - offsetY * zoomFactor));
	event.preventDefault();
	event.stopPropagation();
});

const inBounds = (x, y) => UTILITIES.isBetween(x, -getCanvasOffsetX(), window.innerWidth - getCanvasOffsetX()) && UTILITIES.isBetween(y, -getCanvasOffsetY(), window.innerHeight - getCanvasOffsetY());
const inWindow = (x1, y1, x2, y2) => UTILITIES.isBetween(-getCanvasOffsetX(), x1, x2) || UTILITIES.isBetween(window.innerWidth - getCanvasOffsetX(), x1, x2) || UTILITIES.isBetween(-getCanvasOffsetY(), y1, y2) || UTILITIES.isBetween(window.innerHeight - getCanvasOffsetY(), y1, y2);
const addStation = (key, station) => {
	const {width, height, left, top, angle} = station;
	const blobHeightOffset = (angle === 0 ? height : (width + height - 1) / Math.SQRT2) / 2;
	const blob = new fabric.Rect({
		"originX": "center",
		"originY": "center",
		"width": width,
		"height": height,
		"rx": UTILITIES.size * 6,
		"ry": UTILITIES.size * 6,
		"angle": angle,
		"fill": "white",
		"stroke": "black",
		"strokeWidth": UTILITIES.size * 2,
		"hoverCursor": "pointer",
		"selectable": false,
	});
	const elements = [blob];
	const nameSplit = key.split("|");
	let textYOffset = 0;
	for (let i = 0; i < nameSplit.length; i++) {
		const text = nameSplit[i];
		const fontSize = UTILITIES.size * (UTILITIES.isCJK(text) ? 18 : 9);
		elements.push(new fabric.Text(text, {
			"top": blobHeightOffset + UTILITIES.size * 2 + textYOffset,
			"fontFamily": UTILITIES.fonts.join(","),
			"fontSize": fontSize,
			"fill": "black",
			"stroke": "white",
			"strokeWidth": 4,
			"paintFirst": "stroke",
			"originX": "center",
			"originY": "top",
			"hoverCursor": "pointer",
			"selectable": false,
		}));
		textYOffset += fontSize;
	}
	const group = new fabric.Group(elements, {
		"left": left * zoom,
		"top": top * zoom - blobHeightOffset - UTILITIES.size,
		"topOffset": -blobHeightOffset - UTILITIES.size,
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
	});
	elements.forEach(element => {
		element.on("mouseover", () => group.set("shadow", "black 0 0 8px"));
		element.on("mouseout", () => group.set("shadow", ""));
		element.on("mousedown", () => console.log(UTILITIES.angles.map(angle => station[`routes${angle}`])));
	});
	CANVAS.remove(OBJECT_CACHE[key]);
	OBJECT_CACHE[key] = group;
	group.checkVisibility();
}
const addLine = (key, color, segments) => {
	const line = new fabric.Polyline([], {
		"fill": null,
		"stroke": color,
		"strokeWidth": UTILITIES.size * 6,
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
			UTILITIES.connectLine(point1X, point1Y, point1["direction"], point1["offsetIndex"], point1["routeCount"], point2X, point2Y, point2["direction"], point2["offsetIndex"], point2["routeCount"], UTILITIES.size * 6, newSegments);
			line.points = newSegments;
			return inBounds(point1X, point1Y) || inBounds(point2X, point2Y) || inWindow(point1X, point1Y, point2X, point2Y);
		},
		"checkVisibility": () => {
			const shouldShow = line.updateShape();
			if (CANVAS.getObjects().includes(line)) {
				if (!shouldShow) {
					CANVAS.remove(line);
				}
			} else {
				if (shouldShow) {
					CANVAS.sendToBack(line);
				}
			}
		},
	});
	CANVAS.remove(OBJECT_CACHE[key]);
	OBJECT_CACHE[key] = line;
	line.checkVisibility();
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

const update = () => {
	requestAnimationFrame(update);
	CANVAS.renderAll();
};
update();

export default DRAWING;
