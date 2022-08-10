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
			const segments = [];
			const routeId = references["trips"].find(trip => trip["id"] === tripId)["routeId"];

			for (let i = 0; i < stopTimes.length; i++) {
				const {
					routes0,
					routes45,
					routes90,
					routes135,
					x,
					y,
				} = tempStations[stationIdMap[stopTimes[i]["stopId"]]];
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
				segments.push({
					"x": x,
					"y": y,
					"direction": direction,
					"offsetIndex": offsetIndex,
					"routeCount": routeCount,
				});
			}

			const colorString = references["routes"].find(route => route["id"] === routeId)["color"];
			lineQueue[tripId] = {
				"color": "#" + (colorString === "" ? (Math.round(routeId.replace(/[^0-9]/g, "") / 200 * 0xFFFFFF) % 0xFFFFFF).toString(16) : colorString),
				"segments": segments,
			};
		});

		for (const name in tempStations) {
			const {routes0, routes45, routes90, routes135, x, y} = tempStations[name];
			const rotate = routes45.length + routes135.length > routes0.length + routes90.length;
			const width = Math.max(1, Math.max(routes0.length, routes45.length)) * LINE_WIDTH + LINE_WIDTH;
			const height = Math.max(1, Math.max(routes90.length, routes135.length)) * LINE_WIDTH + LINE_WIDTH;
			const blobHeightOffset = (rotate ? (width + height - 1) / Math.SQRT2 : height) / 2;

			stationQueue[stationNameMap[name]] = {
				"width": width,
				"height": height,
				"left": x,
				"top": y,
				"blobHeightOffset": blobHeightOffset,
				"angle": rotate ? 45 : 0,
				"routes0": routes0,
				"routes45": routes45,
				"routes90": routes90,
				"routes135": routes135,
			};
		}

		const inBounds = (x, y) => UTILITIES.isBetween(x * zoom, -getCanvasOffsetX(), window.innerWidth - getCanvasOffsetX()) && UTILITIES.isBetween(y * zoom, -getCanvasOffsetY(), window.innerHeight - getCanvasOffsetY());
		const inWindow = (x1, y1, x2, y2) => UTILITIES.isBetween(-getCanvasOffsetX(), x1, x2) || UTILITIES.isBetween(window.innerWidth - getCanvasOffsetX(), x1, x2) || UTILITIES.isBetween(-getCanvasOffsetY(), y1, y2) || UTILITIES.isBetween(window.innerHeight - getCanvasOffsetY(), y1, y2);

		for (const key in OBJECT_CACHE) {
			if (!(key in lineQueue) && !(key in stationQueue)) {
				CANVAS.remove(OBJECT_CACHE[key]);
				delete OBJECT_CACHE[key];
			}
		}

		const addLineFromQueue = key => {
			const {color, segments} = lineQueue[key];
			delete lineQueue[key];

			const line = new fabric.Polyline([], {
				"fill": null,
				"stroke": color,
				"strokeWidth": LINE_WIDTH,
				"cornerStyle": "circle",
				"objectCaching": false,
				"hoverCursor": "default",
				"selectable": false,
				"updateShape": () => {
					const newSegments = [];
					let visible = false;
					for (let i = 0; i < segments.length; i++) {
						const prevPoint = i === 0 ? null : segments[i - 1];
						const {x, y, direction, offsetIndex, routeCount} = segments[i];
						const thisX = x * zoom;
						const thisY = y * zoom;

						if (inBounds(x, y)) {
							visible = true;
						}

						if (prevPoint != null) {
							UTILITIES.connectLine(prevPoint["x"] * zoom, prevPoint["y"] * zoom, prevPoint["direction"], prevPoint["offsetIndex"], prevPoint["routeCount"], thisX, thisY, direction, offsetIndex, routeCount, LINE_WIDTH, newSegments);
						}
					}

					line.points = newSegments;
					return visible;
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
		const addStationFromQueue = key => {
			const {
				width,
				height,
				left,
				top,
				blobHeightOffset,
				angle,
				routes0,
				routes45,
				routes90,
				routes135,
			} = stationQueue[key];
			delete stationQueue[key];

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
				"fontFamily": "Noto Sans",
				"fontSize": 16,
				"fill": "black",
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
					const shouldShow = inBounds(left, top);
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

			blob.on("mouseover", () => group.set("shadow", "black 0 0 8px"));
			blob.on("mouseout", () => group.set("shadow", ""));
			blob.on("mousedown", () => console.log(routes0, routes45, routes90, routes135));
			text.on("mouseover", () => group.set("shadow", "black 0 0 8px"));
			text.on("mouseout", () => group.set("shadow", ""));
			text.on("mousedown", () => console.log(routes0, routes45, routes90, routes135));

			CANVAS.remove(OBJECT_CACHE[key]);
			OBJECT_CACHE[key] = group;
			group.checkVisibility();
		};

		setTimeout(() => UTILITIES.runForTime(lineQueue, addLineFromQueue, setTimeout(() => UTILITIES.runForTime(stationQueue, addStationFromQueue, null))));
	},
	drawTestStations: (routes, middleAngle, outerAngles) => {
		CANVAS.clear();
		const getTestStation = (x, y, direction) => {
			const testStation = new fabric.Rect({
				"left": x,
				"top": y,
				"originX": "center",
				"originY": "center",
				"width": LINE_WIDTH * (routes + 1),
				"height": LINE_WIDTH * 2,
				"rx": LINE_WIDTH,
				"ry": LINE_WIDTH,
				"angle": direction,
				"fill": null,
				"stroke": "black",
				"strokeWidth": 2,
				"hoverCursor": "pointer",
				"selectable": x !== 0 && y !== 0,
				"checkVisibility": () => {
					if (!CANVAS.getObjects().includes(testStation)) {
						CANVAS.add(testStation);
					}
				},
			});
			testStation.setControlsVisibility({
				"mt": false,
				"mb": false,
				"ml": false,
				"mr": false,
				"bl": false,
				"br": false,
				"tl": false,
				"tr": false,
				"mtr": false,
			});
			return testStation;
		};
		const getTestLine = (offset, lines, color, station) => {
			const testLine = new fabric.Polyline([], {
				"fill": null,
				"stroke": color,
				"strokeWidth": LINE_WIDTH,
				"cornerStyle": "circle",
				"objectCaching": false,
				"hoverCursor": "default",
				"selectable": false,
				"updateShape": () => {
					const newSegments = [];
					UTILITIES.connectLine(0, 0, middleAngle, offset, lines, station["left"], station["top"], station["angle"], offset, lines, LINE_WIDTH, newSegments);
					testLine.points = newSegments;
					return true;
				},
				"checkVisibility": () => {
					testLine.updateShape();
					if (!CANVAS.getObjects().includes(testLine)) {
						CANVAS.sendToBack(testLine);
					}
				},
			});
			return testLine;
		};
		OBJECT_CACHE["test_station"] = getTestStation(0, 0, middleAngle);
		for (let i = 0; i < 8; i++) {
			const testStation = getTestStation(400 * Math.cos(2 * Math.PI * (i + 0.5) / 8), 400 * Math.sin(2 * Math.PI * (i + 0.5) / 8), outerAngles);
			OBJECT_CACHE[`test_station_${i}`] = testStation;
			for (let j = 0; j < routes; j++) {
				OBJECT_CACHE[`test_line_${i}_${j}`] = getTestLine(j, routes, `#${Math.round(Math.random() * 0xFFFFFF).toString(16).padStart(6, "0")}50`, testStation);
			}
		}
	},
	getCenterLatLon: () => [(getCanvasOffsetY() - window.innerHeight / 2) / zoom, -(getCanvasOffsetX() - window.innerWidth / 2) / zoom, window.innerHeight / zoom, window.innerWidth / zoom],
	viewChanged: () => {
		if (viewChanged) {
			viewChanged = false;
			return true;
		} else {
			return false;
		}
	}
};

const OBJECT_CACHE = {};
let mouseDown = false;
let touchX = 0;
let touchY = 0;
let zoom = 1;
let viewChanged = true;

const CANVAS = new fabric.Canvas("canvas", {renderOnAddRemove: false, selection: false});
CANVAS.absolutePan(new fabric.Point(-window.innerWidth / 2, -window.innerHeight / 2));
const getCanvasOffsetX = () => CANVAS.viewportTransform[4];
const getCanvasOffsetY = () => CANVAS.viewportTransform[5];
const resize = () => {
	CANVAS.setWidth(window.innerWidth);
	CANVAS.setHeight(window.innerHeight);
	viewChanged = true;
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
	if (mouseDown && !UTILITIES.testMode) {
		if ("movementX" in event && "movementY" in event) {
			CANVAS.relativePan(new fabric.Point(event.movementX, event.movementY));
		} else if ("touches" in event && event.touches.length > 0) {
			CANVAS.relativePan(new fabric.Point(event.touches[0].pageX - touchX, event.touches[0].pageY - touchY));
			touchX = event.touches[0].pageX;
			touchY = event.touches[0].pageY;
		}
		viewChanged = true;
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
	viewChanged = true;
	event.preventDefault();
	event.stopPropagation();
});

const checkAllVisibility = () => Object.values(OBJECT_CACHE).forEach(object => object.checkVisibility());
setInterval(checkAllVisibility, UTILITIES.testMode ? 0 : 500);
checkAllVisibility();

const update = () => {
	requestAnimationFrame(update);
	CANVAS.renderAll();
};
requestAnimationFrame(update);

export default DRAWING;
