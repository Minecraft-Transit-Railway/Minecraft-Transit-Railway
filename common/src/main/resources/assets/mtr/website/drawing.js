import SETTINGS from "./index.js";
import CANVAS from "./utilities.js";
import DIRECTIONS from "./directions.js";
import tappable from "./gestures/src/gestures/tap.js";
import panable from "./gestures/src/gestures/pan.js";

const MAX_ARRIVALS = 5;
const FILTER = new PIXI.filters.BlurFilter();
const SEARCH_BOX_ELEMENT = document.getElementById("search_box");
const DIRECTIONS_BOX_1_ELEMENT = document.getElementById("directions_box_1");
const DIRECTIONS_BOX_2_ELEMENT = document.getElementById("directions_box_2");

let graphicsRoutesLayer1 = [];
let graphicsRoutesLayer2 = [];
let graphicsStationsLayer1 = [];
let graphicsStationsLayer2 = [];
let graphicsStationsTextLayer1 = [];
let graphicsStationsTextLayer2 = [];
let textStations = [];

let fetchArrivalId = 0;
let refreshArrivalId = 0;
let arrivalData = [];

const clearAndDestroy = array => {
	for (const index in array) {
		array[index].clear();
		array[index].destroy();
	}
};

const createClickable = (container, initialize, onClick) => {
	const graphics = new PIXI.Graphics();
	graphics.buttonMode = true;
	panable(graphics);
	tappable(graphics);
	graphics.on("panmove", event => CANVAS.onCanvasMouseMove(event, container));
	graphics.on("simpletap", onClick);
	graphics.on("pointerover", () => graphics.filters = [FILTER]);
	graphics.on("pointerout", () => graphics.filters = []);
	initialize(graphics);
	initialize(new PIXI.Graphics());
};

const fetchArrivals = data => {
	if (SETTINGS.selectedStation !== 0) {
		clearTimeout(fetchArrivalId);
		fetch(SETTINGS.arrivalsUrl + "?worldIndex=" + SETTINGS.dimension + "&stationId=" + SETTINGS.selectedStation, {cache: "no-cache"}).then(response => response.json()).then(result => {
			arrivalData = result;
			fetchArrivalId = setTimeout(() => fetchArrivals(data), SETTINGS.refreshArrivalsInterval);
			refreshArrivals(data);
		});
	}
};

const refreshArrivals = data => {
	if (SETTINGS.selectedStation !== 0) {
		clearTimeout(refreshArrivalId);
		const arrivalsHtml = {};
		for (const {arrival, name, destination, circular, platform, route, color} of arrivalData) {
			const currentMillis = Date.now();
			const arrivalDifference = Math.floor((arrival - currentMillis) / 1000);
			const destinationSplit = circular === "" ? destination.split("|") : CANVAS.getClosestInterchangeOnRoute(data, data["routes"].find(checkRoute => checkRoute["name"] === name && checkRoute["color"] === color && checkRoute["circular"] === circular), SETTINGS.selectedStation).split("|");
			const routeNumberSplit = route.split("|");
			if (typeof arrivalsHtml[color] === "undefined") {
				arrivalsHtml[color] = {html: "", count: 0};
			}
			if (arrivalsHtml[color]["count"] < MAX_ARRIVALS) {
				arrivalsHtml[color]["html"] +=
					`<div class="arrival">` +
					`<span class="arrival_text left_align material-icons tight" style="width: ${circular === "" ? 0 : 5}%">${circular === "" ? "" : circular === "cw" ? "rotate_right" : "rotate_left"}</span>` +
					`<span class="arrival_text left_align" style="width: ${circular === "" ? 70 : 65}%">` +
					(route.length === 0 ? "" : routeNumberSplit[Math.floor(currentMillis / 3000) % routeNumberSplit.length] + " ") + destinationSplit[Math.floor(currentMillis / 3000) % destinationSplit.length] +
					`</span>` +
					`<span class="arrival_text" style="width: 10%">${platform}</span>` +
					`<span class="arrival_text right_align" style="width: 20%; text-align: right">${arrivalDifference < 0 ? "" : CANVAS.formatTime(arrivalDifference)}</span>` +
					`</div>`;
				arrivalsHtml[color]["count"]++;
			}
		}
		for (const color in arrivalsHtml) {
			const arrivalElement = document.getElementById("station_arrivals_" + color);
			if (arrivalElement != null) {
				arrivalElement.innerHTML = arrivalsHtml[color]["html"];
			}
		}
		refreshArrivalId = setTimeout(() => refreshArrivals(data), 1000);
	}
};

function drawMap(container, data) {
	const getStationElement = (color, name, id) => {
		const element = document.createElement("div");
		element.setAttribute("id", id);
		element.setAttribute("class", "clickable");
		element.onclick = () => onClickStation(id);
		element.innerHTML =
			(color == null ? "" : `<span class="station" style="background: ${CANVAS.convertColor(color)}"></span>`) +
			`<span class="text">${name.replace(/\|/g, " ")}</span>`;
		return element;
	};

	const getRouteElement = (color, name, type, visible, id, showColor) => {
		const element = document.createElement("div");
		element.setAttribute("id", id);
		element.setAttribute("class", "clickable");
		if (!visible) {
			element.setAttribute("style", "display: none");
		}
		element.onclick = () => onClickLine(color, true);
		element.innerHTML =
			`<span class="line" style="background: ${CANVAS.convertColor(showColor ? color : SETTINGS.getColorStyle("--textColorDisabled"))}"></span>` +
			`<span class="${showColor ? "text" : "text_disabled"} material-icons tight">${SETTINGS.routeTypes[type]}</span>` +
			`<span class="${showColor ? "text" : "text_disabled"}">${name.replace(/\|/g, " ")}</span>`;
		return element;
	};

	const onClickStation = id => {
		SETTINGS.onClearSearch(data, false);
		SETTINGS.clearPanes();
		const {name, color, zone, x, z} = stations[id];
		const stationInfoElement = document.getElementById("station_info");
		stationInfoElement.removeAttribute("style");

		const {xMin, yMin, xMax, yMax} = data["blobs"][id];
		CANVAS.slideTo(container, -(xMin + xMax) / 2 + window.innerWidth / 2, -(yMin + yMax) / 2 + window.innerHeight / 2);

		let stationNameHtml = "";
		const nameSplit = name.split("|");
		for (const nameSplitIndex in nameSplit) {
			const namePart = nameSplit[nameSplitIndex];
			if (SETTINGS.isCJK(namePart)) {
				stationNameHtml += "<h1>" + namePart + "</h1>";
			} else {
				stationNameHtml += "<h2>" + namePart + "</h2>";
			}
		}

		document.getElementById("station_name").innerHTML = stationNameHtml;
		document.getElementById("station_coordinates").innerText = `(${x}, ${z})`;
		document.getElementById("station_zone").innerText = zone;
		document.getElementById("station_line").style.backgroundColor = CANVAS.convertColor(color);
		document.getElementById("station_copy").onclick = event => {
			navigator.clipboard.writeText(`/tp ${x} ~ ${z}`);
			event.target.innerText = "check";
			setTimeout(() => event.target.innerText = "content_copy", 1000);
		};
		document.getElementById("station_directions_1").onclick = () => {
			SETTINGS.clearPanes();
			document.getElementById("directions").style.display = "block";
			DIRECTIONS.onSelectStation(1, id, data);
			document.getElementById("directions_box_2").focus();
		};
		document.getElementById("station_directions_2").onclick = () => {
			SETTINGS.clearPanes();
			document.getElementById("directions").style.display = "block";
			DIRECTIONS.onSelectStation(2, id, data);
			document.getElementById("directions_box_1").focus();
		};

		const stationRoutesElement = document.getElementById("station_routes");
		stationRoutesElement.innerHTML = "";
		const addedRouteColors = [];
		for (const routeIndex in routes) {
			const route = routes[routeIndex];
			if (!addedRouteColors.includes(route["color"])) {
				for (const stationIndex in route["stations"]) {
					if (route["stations"][stationIndex].split("_")[0] === id) {
						stationRoutesElement.append(getRouteElement(route["color"], route["name"].split("||")[0].replace(/\|/g, " "), route["type"], true, "", true, () => onClickLine(container, data, color, true)));
						addedRouteColors.push(route["color"]);

						const arrivalElement = document.createElement("div");
						arrivalElement.id = "station_arrivals_" + route["color"];
						stationRoutesElement.append(arrivalElement);

						const spacerElement = document.createElement("div");
						spacerElement.className = "spacer";
						stationRoutesElement.append(spacerElement);

						break;
					}
				}
			}
		}

		if (stationRoutesElement.lastChild != null) {
			stationRoutesElement.removeChild(stationRoutesElement.lastChild);
		}

		stationInfoElement.style.maxHeight = window.innerHeight - 80 + "px";
		SETTINGS.selectedStation = id;
		fetchArrivals(data);
	};

	const onClickLine = (color, forceClick) => {
		const shouldSelect = forceClick || SETTINGS.selectedColor !== color;
		SETTINGS.onClearSearch(data, false);
		SETTINGS.clearPanes();

		if (shouldSelect) {
			const selectedRoutes = routes.filter(route => route["color"] === color);
			const routeInfoElement = document.getElementById("route_info");
			routeInfoElement.removeAttribute("style");

			let routeNameHtml = "";
			const nameSplit = selectedRoutes[0]["name"].split("||")[0].split("|");
			for (const nameSplitIndex in nameSplit) {
				const namePart = nameSplit[nameSplitIndex];
				if (SETTINGS.isCJK(namePart)) {
					routeNameHtml += "<h1>" + namePart + "</h1>";
				} else {
					routeNameHtml += "<h2>" + namePart + "</h2>";
				}
			}

			document.getElementById("route_name").innerHTML = routeNameHtml;
			document.getElementById("route_line").style.backgroundColor = CANVAS.convertColor(color);

			const routeDetailsElement = document.getElementById("route_stations");
			routeDetailsElement.innerHTML = "";

			selectedRoutes.forEach(route => {
				const {stations, durations, name} = route;
				const routeNameSplit = name.split("||");
				if (routeNameSplit.length > 1) {
					const element = document.createElement("h3");
					element.innerText = routeNameSplit[1].replace(/\|/g, " ");
					routeDetailsElement.appendChild(element);
				}

				const routeStationsElement = document.createElement("div");
				routeStationsElement.className = "station_list"

				for (let i = 0; i < stations.length; i++) {
					const stationId = stations[i].split("_")[0];
					routeStationsElement.appendChild(CANVAS.getDrawStationElement(getStationElement(null, data["stations"][stationId]["name"], stationId), i === 0 ? null : color, i === stations.length - 1 ? null : color));

					if (i < durations.length && durations[i] > 0) {
						const element = document.createElement("span");
						element.innerHTML = CANVAS.formatTime(durations[i] / 20);
						routeStationsElement.appendChild(CANVAS.getDrawLineElement("schedule", element, color));
					}
				}

				routeDetailsElement.appendChild(routeStationsElement);

				const spacerElement = document.createElement("div");
				spacerElement.className = "spacer padded";
				routeDetailsElement.append(spacerElement);
			})

			if (routeDetailsElement.lastChild != null) {
				routeDetailsElement.removeChild(routeDetailsElement.lastChild);
			}

			routeInfoElement.style.maxHeight = window.innerHeight - 80 + "px";
			SETTINGS.selectedColor = color;
		} else {
			SETTINGS.selectedColor = -1;
		}

		SETTINGS.drawDirectionsRoute([], []);
	};

	SEARCH_BOX_ELEMENT.onchange = () => onSearch(data);
	SEARCH_BOX_ELEMENT.onpaste = () => onSearch(data);
	SEARCH_BOX_ELEMENT.oninput = () => onSearch(data);
	DIRECTIONS_BOX_1_ELEMENT.onchange = () => DIRECTIONS.onSearch(1, data);
	DIRECTIONS_BOX_1_ELEMENT.onpaste = () => DIRECTIONS.onSearch(1, data);
	DIRECTIONS_BOX_1_ELEMENT.oninput = () => DIRECTIONS.onSearch(1, data);
	DIRECTIONS_BOX_2_ELEMENT.onchange = () => DIRECTIONS.onSearch(2, data);
	DIRECTIONS_BOX_2_ELEMENT.onpaste = () => DIRECTIONS.onSearch(2, data);
	DIRECTIONS_BOX_2_ELEMENT.oninput = () => DIRECTIONS.onSearch(2, data);

	clearAndDestroy(graphicsRoutesLayer1);
	clearAndDestroy(graphicsRoutesLayer2);
	clearAndDestroy(graphicsStationsLayer1);
	clearAndDestroy(graphicsStationsLayer2);
	clearAndDestroy(graphicsStationsTextLayer1);
	clearAndDestroy(graphicsStationsTextLayer2);
	graphicsRoutesLayer1 = [];
	graphicsRoutesLayer2 = [];
	graphicsStationsLayer1 = [];
	graphicsStationsLayer2 = [];
	graphicsStationsTextLayer1 = [];
	graphicsStationsTextLayer2 = [];
	textStations = [];
	container.children = [];

	data["blobs"] = {};
	const {blobs, positions, stations, routes, types} = data;

	for (const stationId in stations) {
		const station = stations[stationId];
		const horizontal = station["horizontal"].filter(element => element["types"].some(type => SETTINGS.selectedRouteTypes.includes(type)));
		const vertical = station["vertical"].filter(element => element["types"].some(type => SETTINGS.selectedRouteTypes.includes(type)));

		if (horizontal.length === 0 && vertical.length === 0) {
			blobs[stationId] = {
				xMin: CANVAS.convertX(stations[stationId]["x"]),
				yMin: CANVAS.convertY(stations[stationId]["z"]),
				xMax: CANVAS.convertX(stations[stationId]["x"]),
				yMax: CANVAS.convertY(stations[stationId]["z"]),
				name: stations[stationId]["name"],
				colors: [],
			};
		} else {
			const sort = (a, b, key) => a[key] === b[key] ? a["color"] - b["color"] : a[key] - b[key];
			horizontal.sort((a, b) => sort(a, b, "y"));
			vertical.sort((a, b) => sort(a, b, "x"));

			let xCount;
			let x = 0;
			if (vertical.length === 0) {
				horizontal.forEach(element => x += element["x"]);
				xCount = 1;
				x /= horizontal.length;
			} else {
				vertical.forEach(element => x += element["x"]);
				xCount = vertical.length;
				x /= xCount;
			}

			let yCount;
			let y = 0;
			if (horizontal.length === 0) {
				vertical.forEach(element => y += element["y"]);
				yCount = 1;
				y /= vertical.length;
			} else {
				horizontal.forEach(element => y += element["y"]);
				yCount = horizontal.length;
				y /= yCount;
			}

			x = CANVAS.convertX(x);
			y = CANVAS.convertY(y);

			const colors = [];
			for (let i = 0; i < horizontal.length; i++) {
				const color = horizontal[i]["color"];
				colors.push(color);
				const position = positions[stationId + "_" + color];
				position["x2"] = x;
				position["y2"] = y + (i - (horizontal.length - 1) / 2) * SETTINGS.lineSize;
			}
			for (let i = 0; i < vertical.length; i++) {
				const color = vertical[i]["color"];
				colors.push(color);
				const position = positions[stationId + "_" + color];
				position["x2"] = x + (i - (vertical.length - 1) / 2) * SETTINGS.lineSize;
				position["y2"] = y;
			}

			const xOffset = SETTINGS.lineSize * (xCount - 1) / 2;
			const yOffset = SETTINGS.lineSize * (yCount - 1) / 2;
			blobs[stationId] = {
				xMin: x - xOffset,
				yMin: y - yOffset,
				xMax: x + xOffset,
				yMax: y + yOffset,
				name: stations[stationId]["name"],
				colors: colors,
			};
		}
	}

	const routeNames = {};
	const routeTypes = {};
	const sortedColors = [];
	for (const routeIndex in routes) {
		const route = routes[routeIndex];
		const color = route["color"];
		const shouldDraw = SETTINGS.selectedDirectionsStations.length === 0 && (SETTINGS.selectedColor < 0 || SETTINGS.selectedColor === color);
		const routeType = route["type"];

		for (const stationIndex in route["stations"]) {
			const blob = blobs[route["stations"][stationIndex].split("_")[0]];
			if (typeof blob !== "undefined") {
				blob[routeType] = true;
			}
		}

		if (SETTINGS.selectedRouteTypes.includes(routeType)) {
			createClickable(container, graphicsRoute => {
				(shouldDraw ? graphicsRoutesLayer2 : graphicsRoutesLayer1).push(graphicsRoute);
				graphicsRoute.beginFill(shouldDraw ? color : SETTINGS.getColorStyle("--textColorDisabled"));

				let prevX = undefined;
				let prevY = undefined;
				let prevVertical = undefined;
				for (const stationIndex in route["stations"]) {
					const id = route["stations"][stationIndex];
					const {x2, y2, vertical} = positions[id];

					if (typeof prevX !== "undefined" && typeof prevY !== "undefined") {
						CANVAS.drawLine(graphicsRoute, prevX, prevY, prevVertical, x2, y2, vertical);
					}

					prevX = x2;
					prevY = y2;
					prevVertical = vertical;
				}

				graphicsRoute.endFill();
			}, () => onClickLine(color, false));

			if (color in SETTINGS.selectedDirectionsSegments) {
				const graphics = new PIXI.Graphics();
				graphicsStationsLayer2.push(graphics);
				graphics.beginFill(color);

				SETTINGS.selectedDirectionsSegments[color].forEach(segment => {
					const position1 = positions[segment.split("_")[0] + "_" + color];
					const position2 = positions[segment.split("_")[1] + "_" + color];
					CANVAS.drawLine(graphics, position1["x2"], position1["y2"], position1["vertical"], position2["x2"], position2["y2"], position2["vertical"]);
				});

				graphics.endFill();
			}
		}

		routeNames[color] = route["name"].split("||")[0];
		routeTypes[color] = routeType;
		if (!sortedColors.includes(color)) {
			sortedColors.push(color);
		}
	}
	sortedColors.sort();

	for (const stationId in blobs) {
		const blob = blobs[stationId];
		const {xMin, yMin, xMax, yMax, colors, name} = blob;
		if (SETTINGS.selectedRouteTypes.some(routeType => blob[routeType])) {
			const shouldDraw = SETTINGS.selectedDirectionsStations.length > 0 ? SETTINGS.selectedDirectionsStations.includes(stationId) : SETTINGS.selectedColor < 0 || colors.includes(SETTINGS.selectedColor);

			createClickable(container, graphicsStation => {
				(shouldDraw ? graphicsStationsLayer2 : graphicsStationsLayer1).push(graphicsStation);
				graphicsStation.beginFill(SETTINGS.getColorStyle("--backgroundColor"));
				graphicsStation.lineStyle(2, SETTINGS.getColorStyle(shouldDraw ? "--textColor" : "--textColorDisabled"), 1);

				if (xMin === xMax && yMin === yMax) {
					graphicsStation.drawCircle(xMin, yMin, SETTINGS.lineSize);
				} else {
					graphicsStation.drawRoundedRect(xMin - SETTINGS.lineSize, yMin - SETTINGS.lineSize, xMax - xMin + SETTINGS.lineSize * 2, yMax - yMin + SETTINGS.lineSize * 2, SETTINGS.lineSize);
				}
				graphicsStation.endFill();
			}, () => onClickStation(stationId));

			if (SETTINGS.showText && shouldDraw) {
				let icons = "";
				types.forEach(key => {
					if (typeof blob[key] !== "undefined" && !SETTINGS.selectedRouteTypes.includes(key)) {
						icons += SETTINGS.routeTypes[key];
					}
				});
				CANVAS.drawText(textStations, stationId, name, icons, (xMin + xMax) / 2, yMax + SETTINGS.lineSize);
			}
		}
	}

	for (const index in graphicsRoutesLayer1) {
		container.addChild(graphicsRoutesLayer1[index]);
	}
	for (const index in graphicsStationsLayer1) {
		container.addChild(graphicsStationsLayer1[index]);
	}
	for (const index in graphicsRoutesLayer2) {
		container.addChild(graphicsRoutesLayer2[index]);
	}
	for (const index in graphicsStationsLayer2) {
		container.addChild(graphicsStationsLayer2[index]);
	}
	for (const index in textStations) {
		container.addChild(textStations[index]);
	}

	const elementRoutes = document.getElementById("search_results_routes");
	elementRoutes.innerHTML = "";
	for (const colorIndex in sortedColors) {
		const color = sortedColors[colorIndex];
		elementRoutes.append(getRouteElement(color, routeNames[color], routeTypes[color], false, color, SETTINGS.selectedColor < 0 || SETTINGS.selectedColor === color));
	}
	const elementStations = document.getElementById("search_results_stations");
	elementStations.innerHTML = "";
	for (const stationId in stations) {
		const element = getStationElement(stations[stationId]["color"], stations[stationId]["name"], stationId);
		element.setAttribute("style", "display: none");
		elementStations.append(element);
	}
	DIRECTIONS.writeStationsInResult(1, data);
	DIRECTIONS.writeStationsInResult(2, data);

	document.getElementById("loading").style.display = "none";
	onSearch(data);
}

function onSearch(data) {
	const searchBox = document.getElementById("search_box");
	const search = searchBox.value.toLowerCase().replace(/\|/g, " ");
	document.getElementById("clear_search_icon").style.display = search === "" ? "none" : "";

	const {stations, routes} = data;

	const resultsStations = search === "" ? [] : Object.keys(stations).filter(station => stations[station]["name"].replace(/\|/g, " ").toLowerCase().includes(search));
	for (const stationId in stations) {
		document.getElementById(stationId).style.display = resultsStations.includes(stationId) ? "block" : "none";
	}

	const resultsRoutes = search === "" ? [] : Object.keys(routes).filter(route => routes[route]["name"].toLowerCase().includes(search));
	for (const routeIndex in routes) {
		document.getElementById(routes[routeIndex]["color"]).style.display = resultsRoutes.includes(routeIndex) ? "block" : "none";
	}

	const maxHeight = (window.innerHeight - 80) / 2;
	document.getElementById("search_results_stations").style.maxHeight = maxHeight + "px";
	document.getElementById("search_results_routes").style.maxHeight = maxHeight + "px";

	if (search !== "") {
		SETTINGS.clearPanes();
	}
}

export default drawMap;
