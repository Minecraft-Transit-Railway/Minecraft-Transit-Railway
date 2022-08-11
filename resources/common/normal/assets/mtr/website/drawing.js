import UTILITIES from "./utilities.js";

const LINE_WIDTH = 6;
const DRAWING = {
	addStations: (result, latOffset, lonOffset, zoomOffset) => {
		const {list, references} = result["data"];
		const tempStations = {};
		const stationIdMap = {};
		const stationNameMap = {};
		const lineQueue = {};
		const stationQueue = {};

		references["stops"].forEach(data => {
			const {name, id, direction, lat, lon, routeIds} = data;
			const newName = name.split(" ").sort().join(" ");

			if (!(newName in tempStations)) {
				tempStations[newName] = {
					"routes0": [],
					"routes45": [],
					"routes90": [],
					"routes135": [],
					"latValues": [],
					"lonValues": [],
				};
			}

			const key = direction === "NE" || direction === "SW" ? "routes45" : direction === "E" || direction === "W" ? "routes90" : direction === "SE" || direction === "NW" ? "routes135" : "routes0";
			routeIds.forEach(routeId => {
				if (!tempStations[newName]["routes0"].includes(routeId) && !tempStations[newName]["routes45"].includes(routeId) && !tempStations[newName]["routes90"].includes(routeId) && !tempStations[newName]["routes135"].includes(routeId)) {
					tempStations[newName][key].push(routeId);
				}
			});
			tempStations[newName]["latValues"].push((lat - latOffset) * zoomOffset);
			tempStations[newName]["lonValues"].push((lon - lonOffset) * zoomOffset);
			stationIdMap[id] = newName;
			stationNameMap[newName] = name;
		});

		const listAverage = list => list.length === 0 ? 0 : list.reduce((a, b) => a + b, 0) / list.length;
		Object.values(tempStations).forEach(tempStation => {
			tempStation["routes0"] = tempStation["routes0"].sort();
			tempStation["routes45"] = tempStation["routes45"].sort();
			tempStation["routes90"] = tempStation["routes90"].sort();
			tempStation["routes135"] = tempStation["routes135"].sort();
			tempStation["x"] = listAverage(tempStation["lonValues"]);
			tempStation["y"] = -listAverage(tempStation["latValues"]);
			delete tempStation["lonValues"];
			delete tempStation["latValues"];
		});

		list.forEach(data => {
			const {schedule, tripId} = data;
			const {stopTimes} = schedule;
			const routeId = references["trips"].find(trip => trip["id"] === tripId)["routeId"];
			const colorString = references["routes"].find(route => route["id"] === routeId)["color"];
			const color = colorString === "" ? (Math.round(routeId.replace(/[^0-9]/g, "") / 200 * 0xFFFFFF) % 0xFFFFFF).toString(16) : colorString;
			const getSegmentDetails = stopId => {
				const {
					routes0,
					routes45,
					routes90,
					routes135,
					x,
					y,
				} = tempStations[stationIdMap[stopId]];
				let direction;
				let offsetIndex;
				let routeCount;
				if (routes45.includes(routeId)) {
					direction = 45;
					offsetIndex = routes45.indexOf(routeId);
					routeCount = routes45.length;
				} else if (routes90.includes(routeId)) {
					direction = 90;
					offsetIndex = routes90.indexOf(routeId);
					routeCount = routes90.length;
				} else if (routes135.includes(routeId)) {
					direction = 135;
					offsetIndex = routes135.indexOf(routeId);
					routeCount = routes135.length;
				} else {
					direction = 0;
					offsetIndex = routes0.indexOf(routeId);
					routeCount = routes0.length;
				}
				return {
					"x": x,
					"y": y,
					"direction": direction,
					"offsetIndex": offsetIndex,
					"routeCount": routeCount,
				};
			}

			for (let i = 0; i < stopTimes.length - 1; i++) {
				const stopId1 = stopTimes[i]["stopId"];
				const stopId2 = stopTimes[i + 1]["stopId"];
				if (stopId1 !== stopId2) {
					const key = color + [stopId1, stopId2].sort().join(" ");
					if (!(key in lineQueue)) {
						lineQueue[key] = {
							"color": `#${color}`,
							"segments": [getSegmentDetails(stopId1), getSegmentDetails(stopId2)],
						};
					}
				}
			}
		});

		for (const name in tempStations) {
			const {routes0, routes45, routes90, routes135, x, y} = tempStations[name];
			const rotate = routes45.length + routes135.length > routes0.length + routes90.length;
			const width = Math.max(1, Math.max(routes0.length, routes45.length)) * LINE_WIDTH + LINE_WIDTH;
			const height = Math.max(1, Math.max(routes90.length, routes135.length)) * LINE_WIDTH + LINE_WIDTH;

			stationQueue[stationNameMap[name]] = {
				"width": width,
				"height": height,
				"left": x,
				"top": y,
				"angle": rotate ? 45 : 0,
				"routes0": routes0,
				"routes45": routes45,
				"routes90": routes90,
				"routes135": routes135,
			};
		}

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
			const {
				width,
				height,
				left,
				top,
				angle,
				routes0,
				routes45,
				routes90,
				routes135,
			} = stationQueue[key];
			addStation(key, width, height, left, top, angle, routes0, routes45, routes90, routes135);
			delete stationQueue[key];
		};
		setTimeout(() => UTILITIES.runForTime(stationQueue, addStationFromQueue, setTimeout(() => UTILITIES.runForTime(lineQueue, addLineFromQueue, null))));
	},
	drawTestStations: (routes, middleAngle, outerAngles) => {
		CANVAS.clear();
		addStation("Middle", LINE_WIDTH * (routes + 1), LINE_WIDTH * 2, 0, 0, middleAngle, [], [], [], []);
		for (let i = 0; i < 8; i++) {
			const stationX = 400 * Math.cos(2 * Math.PI * (i + 0.5) / 8);
			const stationY = 400 * Math.sin(2 * Math.PI * (i + 0.5) / 8);
			addStation(`Outer ${i}`, LINE_WIDTH * (routes + 1), LINE_WIDTH * 2, stationX, stationY, outerAngles, [], [], [], []);
			for (let j = 0; j < routes; j++) {
				addLine(`test_line_${i}_${j}`, `#${Math.round(Math.random() * 0xFFFFFF).toString(16).padStart(6, "0")}50`, [
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

const OBJECT_CACHE = {};
const MAX_OBJECTS = 500;
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
const addStation = (key, width, height, left, top, angle, routes0, routes45, routes90, routes135) => {
	const blobHeightOffset = (angle === 0 ? height : (width + height - 1) / Math.SQRT2) / 2;
	const blob = new fabric.Rect({
		"originX": "center",
		"originY": "center",
		"width": width,
		"height": height,
		"rx": LINE_WIDTH,
		"ry": LINE_WIDTH,
		"angle": angle,
		"fill": "white",
		"stroke": "black",
		"strokeWidth": 2,
		"hoverCursor": "pointer",
		"selectable": false,
	});
	const text = new fabric.Text(key, {
		"top": blobHeightOffset + 2,
		"fontFamily": UTILITIES.fonts.join(","),
		"fontSize": 16,
		"fill": "black",
		"stroke": "white",
		"strokeWidth": 4,
		"paintFirst": "stroke",
		"originX": "center",
		"originY": "top",
		"hoverCursor": "pointer",
		"selectable": false,
	});
	const group = new fabric.Group([blob, text], {
		"left": left * zoom,
		"top": top * zoom - blobHeightOffset - 1,
		"topOffset": -blobHeightOffset - 1,
		"originX": "center",
		"originY": "top",
		"subTargetCheck": true,
		"hoverCursor": "default",
		"selectable": false,
		"checkVisibility": () => {
			const shouldShow = inBounds(left * zoom, top * zoom);
			const canvasObjects = CANVAS.getObjects().length;
			if (CANVAS.getObjects().includes(group)) {
				if (!shouldShow || canvasObjects > MAX_OBJECTS) {
					CANVAS.remove(group);
				}
			} else {
				if (shouldShow && canvasObjects < MAX_OBJECTS) {
					CANVAS.add(group);
				}
			}
		},
	});
	blob.on("mouseover", () => group.set("shadow", "black 0 0 8px"));
	blob.on("mouseout", () => group.set("shadow", ""));
	blob.on("mousedown", () => console.log(routes0, routes45, routes90, routes135));
	text.on("mouseover", () => group.set("shadow", "black 0 0 8px"));
	text.on("mouseout", () => group.set("shadow", ""));
	text.on("mousedown", () => console.log(routes0, routes45, routes90, routes135));
	CANVAS.remove(OBJECT_CACHE[key]);
	OBJECT_CACHE[key] = group;
	group.checkVisibility();
}
const addLine = (key, color, segments) => {
	const line = new fabric.Polyline([], {
		"fill": null,
		"stroke": color,
		"strokeWidth": LINE_WIDTH,
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
			UTILITIES.connectLine(point1X, point1Y, point1["direction"], point1["offsetIndex"], point1["routeCount"], point2X, point2Y, point2["direction"], point2["offsetIndex"], point2["routeCount"], LINE_WIDTH, newSegments);
			line.points = newSegments;
			return inBounds(point1X, point1Y) || inBounds(point2X, point2Y) || inWindow(point1X, point1Y, point2X, point2Y);
		},
		"checkVisibility": () => {
			const shouldShow = line.updateShape();
			const canvasObjects = CANVAS.getObjects().length;
			if (CANVAS.getObjects().includes(line)) {
				if (!shouldShow || canvasObjects > MAX_OBJECTS) {
					CANVAS.remove(line);
				}
			} else {
				if (shouldShow && canvasObjects < MAX_OBJECTS) {
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
