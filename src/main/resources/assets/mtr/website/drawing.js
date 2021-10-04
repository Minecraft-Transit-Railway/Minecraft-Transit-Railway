{
	let graphicsRoutesLayer1 = [];
	let graphicsRoutesLayer2 = [];
	let graphicsStationsLayer1 = [];
	let graphicsStationsLayer2 = [];
	let graphicsStationsTextLayer1 = [];
	let graphicsStationsTextLayer2 = [];
	let textStations = [];

	let selecting = false;
	let fetchArrivalId = 0;
	let refreshArrivalId = 0;
	let arrivalData = [];

	const clearAndDestroy = (array, canClear) => {
		for (const index in array) {
			if (canClear) {
				array[index].clear();
			}
			array[index].destroy();
		}
	};

	const createClickable = (container, initialize, onClick) => {
		const graphics = new PIXI.Graphics();
		graphics.interactive = true;
		graphics.buttonMode = true;
		graphics.on("pointerdown", event => {
			onCanvasMouseDown(event);
			selecting = true;
		});
		graphics.on("pointermove", event => {
			onCanvasMouseMove(event, container);
			selecting = false;
		});
		graphics.on("pointerup", event => {
			onCanvasMouseUp(event);
			if (selecting) {
				onClick();
			}
			selecting = false;
		});
		graphics.on("pointerover", () => graphics.filters = [FILTER]);
		graphics.on("pointerout", () => graphics.filters = []);
		initialize(graphics);
		initialize(new PIXI.Graphics());
	};

	const fetchArrivals = () => {
		if (selectedStation !== 0) {
			clearTimeout(fetchArrivalId);
			fetch(ARRIVALS_URL + "?worldIndex=" + dimension + "&stationId=" + selectedStation, {cache: "no-cache"}).then(response => response.json()).then(result => {
				arrivalData = result;
				fetchArrivalId = setTimeout(fetchArrivals, REFRESH_INTERVAL);
				refreshArrivals();
			});
		}
	};

	const refreshArrivals = () => {
		if (selectedStation !== 0) {
			clearTimeout(refreshArrivalId);
			const arrivalsHtml = {};
			for (const arrivalIndex in arrivalData) {
				const {arrival, destination, platform, color} = arrivalData[arrivalIndex];
				const currentMillis = Date.now();
				const arrivalDifference = Math.floor((arrival - currentMillis) / 1000);
				const minute = Math.floor(arrivalDifference / 60);
				const second = (arrivalDifference % 60).toString().padStart(2, "0");
				const destinationSplit = destination.split("|");
				if (arrivalsHtml[color] === undefined) {
					arrivalsHtml[color] = "";
				}
				arrivalsHtml[color] +=
					`<div class="arrival">` +
					`<span class="arrival_text left_align" style="width: 70%">${destinationSplit[Math.floor(currentMillis / 3000) % destinationSplit.length]}</span>` +
					`<span class="arrival_text" style="width: 10%">${platform}</span>` +
					`<span class="arrival_text right_align" style="width: 20%; text-align: right">${arrivalDifference < 0 ? "" : minute + ":" + second}</span>` +
					`</div>`;
			}
			for (const color in arrivalsHtml) {
				const arrivalElement = document.getElementById("station_arrivals_" + color);
				if (arrivalElement != null) {
					arrivalElement.innerHTML = arrivalsHtml[color];
				}
			}
			refreshArrivalId = setTimeout(refreshArrivals, 1000);
		}
	};

	function drawMap(container, data) {
		const getStationElement = (color, name, id) => {
			const element = document.createElement("div");
			element.setAttribute("id", id);
			element.setAttribute("class", "clickable");
			element.setAttribute("style", "display: none");
			element.onclick = () => onClickStation(id);
			element.innerHTML =
				`<span class="station" style="background: ${convertColor(color)}"></span>` +
				`<span class="text">${name.replaceAll("|", " ")}</span>`;
			return element;
		};

		const getRouteElement = (color, name, visible, id, showColor) => {
			const element = document.createElement("div");
			element.setAttribute("id", id);
			element.setAttribute("class", "clickable");
			if (!visible) {
				element.setAttribute("style", "display: none");
			}
			element.onclick = () => onClickLine(color);
			element.innerHTML =
				`<span class="line" style="background: ${convertColor(showColor ? color : getColorStyle("--textColorDisabled"))}"></span>` +
				`<span class="${showColor ? "text" : "text_disabled"}">${name.replaceAll("|", " ")}</span>`;
			return element;
		};

		const onClickStation = id => {
			onClearSearch(data);
			const {name, color} = stations[id];
			const stationInfoElement = document.getElementById("station_info");
			stationInfoElement.removeAttribute("style");

			const {xMin, yMin, xMax, yMax} = data["blobs"][id];
			container.x = Math.round(-(xMin + xMax) / 2 + window.innerWidth / 2);
			container.y = Math.round(-(yMin + yMax) / 2 + window.innerHeight / 2);

			let stationNameHtml = "";
			const nameSplit = name.split("|");
			for (const nameSplitIndex in nameSplit) {
				const namePart = nameSplit[nameSplitIndex];
				if (isCJK(namePart)) {
					stationNameHtml += "<h1>" + namePart + "</h1>";
				} else {
					stationNameHtml += "<h2>" + namePart + "</h2>";
				}
			}
			document.getElementById("station_name").innerHTML = stationNameHtml;

			document.getElementById("station_line").style.backgroundColor = convertColor(color);

			const stationRoutesElement = document.getElementById("station_routes");
			stationRoutesElement.innerHTML = "";
			const addedRouteColors = [];
			for (const routeIndex in routes) {
				const route = routes[routeIndex];
				if (!addedRouteColors.includes(route["color"])) {
					for (const stationIndex in route["stations"]) {
						if (route["stations"][stationIndex].split("_")[0] === id) {
							stationRoutesElement.append(getRouteElement(route["color"], route["name"].replaceAll("|", " "), true, "", true, () => onClickLine(container, data, color)));
							addedRouteColors.push(route["color"]);

							const arrivalElement = document.createElement("div");
							arrivalElement.setAttribute("id", "station_arrivals_" + route["color"]);
							stationRoutesElement.append(arrivalElement);

							const spacerElement = document.createElement("div");
							spacerElement.setAttribute("class", "spacer");
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
			selectedStation = id;
			fetchArrivals();
		};

		const onClickLine = color => {
			selectedColor = selectedColor === color ? -1 : color;
			drawMap(container, data);
		};

		clearAndDestroy(graphicsRoutesLayer1, true);
		clearAndDestroy(graphicsRoutesLayer2, true);
		clearAndDestroy(graphicsStationsLayer1, true);
		clearAndDestroy(graphicsStationsLayer2, true);
		clearAndDestroy(graphicsStationsTextLayer1, true);
		clearAndDestroy(graphicsStationsTextLayer2, true);
		clearAndDestroy(textStations, false);
		clearAndDestroy(container.children, true);
		graphicsRoutesLayer1 = [];
		graphicsRoutesLayer2 = [];
		graphicsStationsLayer1 = [];
		graphicsStationsLayer2 = [];
		graphicsStationsTextLayer1 = [];
		graphicsStationsTextLayer2 = [];
		textStations = [];
		container.children = [];

		data["blobs"] = {};
		const {blobs, positions, stations, routes} = data;

		const visitedPositions = {};
		for (const positionKey in positions) {
			const position = positions[positionKey];
			const x = convertX(position["x"]);
			const y = convertY(position["y"]);

			const stationId = positionKey.split("_")[0];
			const color = parseInt(positionKey.split("_")[1]);

			let newX = x;
			let newY = y;
			if (position["vertical"]) {
				let i = 1;
				while (visitedPositions[stationId + "_x_" + newX] !== undefined && visitedPositions[stationId + "_x_" + newX] !== color) {
					newX = x + i * LINE_SIZE;
					i = i > 0 ? -i : -i + 1;
				}
				visitedPositions[stationId + "_x_" + newX] = color;
			} else {
				let i = 1;
				while (visitedPositions[stationId + "_y_" + newY] !== undefined && visitedPositions[stationId + "_y_" + newY] !== color) {
					newY = y + i * LINE_SIZE;
					i = i > 0 ? -i : -i + 1;
				}
				visitedPositions[stationId + "_y_" + newY] = color;
			}

			position["x2"] = newX;
			position["y2"] = newY;

			let blob = blobs[stationId];
			if (blob === undefined) {
				blobs[stationId] = {
					xMin: newX,
					yMin: newY,
					xMax: newX,
					yMax: newY,
					name: stations[stationId]["name"],
					colors: [color],
				};
			} else {
				blob["xMin"] = Math.min(blob["xMin"], newX);
				blob["yMin"] = Math.min(blob["yMin"], newY);
				blob["xMax"] = Math.max(blob["xMax"], newX);
				blob["yMax"] = Math.max(blob["yMax"], newY);
				if (!blob["colors"].includes(color)) {
					blob["colors"].push(color);
				}
			}
		}

		const routeNames = {};
		const sortedColors = [];
		for (const routeKey in routes) {
			const route = routes[routeKey];
			const color = route["color"];
			const shouldDraw = selectedColor < 0 || selectedColor === color;

			createClickable(container, graphicsRoute => {
				(shouldDraw ? graphicsRoutesLayer2 : graphicsRoutesLayer1).push(graphicsRoute);
				graphicsRoute.beginFill(shouldDraw ? color : getColorStyle("--textColorDisabled"));

				let prevX = undefined;
				let prevY = undefined;
				let prevVertical = undefined;
				for (const stationIndex in route["stations"]) {
					const id = route["stations"][stationIndex];
					const {x2, y2, vertical} = positions[id];

					if (prevX !== undefined && prevY !== undefined) {
						drawLine(graphicsRoute, prevX, prevY, prevVertical, x2, y2, vertical);
					}

					prevX = x2;
					prevY = y2;
					prevVertical = vertical;
				}

				graphicsRoute.endFill();
			}, () => onClickLine(color));

			routeNames[color] = route["name"];
			if (!sortedColors.includes(color)) {
				sortedColors.push(color);
			}
		}
		sortedColors.sort();

		for (const stationId in blobs) {
			const {xMin, yMin, xMax, yMax, colors} = blobs[stationId];
			const shouldDraw = selectedColor < 0 || colors.includes(selectedColor);

			createClickable(container, graphicsStation => {
				(shouldDraw ? graphicsStationsLayer2 : graphicsStationsLayer1).push(graphicsStation);
				graphicsStation.beginFill(getColorStyle("--backgroundColor"));
				graphicsStation.lineStyle(2, getColorStyle(shouldDraw ? "--textColor" : "--textColorDisabled"), 1);

				if (xMin === xMax && yMin === yMax) {
					graphicsStation.drawCircle(xMin, yMin, LINE_SIZE);
				} else {
					graphicsStation.drawRoundedRect(xMin - LINE_SIZE, yMin - LINE_SIZE, xMax - xMin + LINE_SIZE * 2, yMax - yMin + LINE_SIZE * 2, LINE_SIZE);
				}
				graphicsStation.endFill();
			}, () => onClickStation(stationId));

			if (shouldDraw) {
				drawText(textStations, blobs[stationId]["name"], (xMin + xMax) / 2, yMax + LINE_SIZE);
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
			elementRoutes.append(getRouteElement(color, routeNames[color], false, color, selectedColor < 0 || selectedColor === color));
		}
		const elementStations = document.getElementById("search_results_stations");
		elementStations.innerHTML = "";
		for (const stationId in stations) {
			elementStations.append(getStationElement(stations[stationId]["color"], stations[stationId]["name"], stationId));
		}

		onSearch(data);
	}
}
