const URL = "http://" + document.location.host;
const DATA_URL = URL + "/data"
const ARRIVALS_URL = URL + "/arrivals";
const REFRESH_INTERVAL = 5000;

const SCALE_UPPER_LIMIT = 64;
const SCALE_LOWER_LIMIT = 1 / 128;

const LINE_SIZE = 6;
const TEXT_PADDING = 6;

const convertX = x => Math.floor(x * scale / LINE_SIZE) * LINE_SIZE - canvasOffsetX * scale + window.innerWidth / 2;
const convertY = y => Math.floor(y * scale / LINE_SIZE) * LINE_SIZE - canvasOffsetY * scale + window.innerHeight / 2;
const convertColor = color => "#" + Number(color).toString(16).padStart(6, "0");
const isBetween = (x, a, b) => x >= Math.min(a, b) && x <= Math.max(a, b);
const isCJK = text => text.match(/[\u3000-\u303f\u3040-\u309f\u30a0-\u30ff\uff00-\uff9f\u4e00-\u9faf\u3400-\u4dbf]/);

let json;
let dragging = false;
let selecting = false;
let mouseClickX = 0;
let mouseClickY = 0;
let canvasOffsetX = 0;
let canvasOffsetY = 0;
let scale = 1;
let selectedColor = "";
let selectedStation = 0;
let fetchArrivalId = 0;
let refreshArrivalId = 0;
let arrivalData = [];
let dimension = 0; // TODO other dimensions

window.onload = () => {
    window.addEventListener("resize", callback);
    const canvas = document.getElementById("map");
    canvas.addEventListener("mousedown", onCanvasMouseDown);
    canvas.addEventListener("mousemove", onCanvasMouseMove);
    canvas.addEventListener("mouseup", onCanvasMouseUp);
    canvas.addEventListener("mouseleave", onCanvasMouseUp);
    canvas.addEventListener("wheel", onCanvasScroll);
}

refresh();
setInterval(refresh, REFRESH_INTERVAL);

function refresh() {
    fetch(DATA_URL, {cache: "no-cache"}).then(response => response.json()).then(result => {
        json = result;
        callback();
    });
}

function callback() {
    const context = document.getElementById("map").getContext("2d");
    context.canvas.width = window.innerWidth;
    context.canvas.height = window.innerHeight;

    const data = json[dimension];
    data["blobs"] = {};
    const {blobs, positions, stations, routes} = data;


    const visitedPositions = {};
    for (const positionKey in positions) {
        const position = positions[positionKey];
        const x = convertX(position["x"]);
        const y = convertY(position["y"]);

        const stationId = positionKey.split("_")[0];
        const color = positionKey.split("_")[1];
        const colorConverted = convertColor(color);

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
                colors: [colorConverted],
            };
        } else {
            blob["xMin"] = Math.min(blob["xMin"], newX);
            blob["yMin"] = Math.min(blob["yMin"], newY);
            blob["xMax"] = Math.max(blob["xMax"], newX);
            blob["yMax"] = Math.max(blob["yMax"], newY);
            if (!blob["colors"].includes(colorConverted)) {
                blob["colors"].push(colorConverted);
            }
        }
    }

    const routeNames = {};
    const sortedColors = [];
    const visibleLegendColors = [];
    for (const routeKey in routes) {
        const route = routes[routeKey];
        const color = convertColor(route["color"]);

        beginDraw(context, selectedColor === "" || selectedColor === color ? color : "lightgray", LINE_SIZE);

        let prevX = undefined;
        let prevY = undefined;
        let prevVertical = undefined;
        for (const stationIndex in route["stations"]) {
            const id = route["stations"][stationIndex];
            const x = positions[id]["x2"];
            const y = positions[id]["y2"];
            const vertical = positions[id]["vertical"];

            if (prevX === undefined || prevY === undefined) {
                context.moveTo(x, y);
            } else {
                drawLine(context, prevX, prevY, prevVertical, x, y, vertical);
            }

            if (!visibleLegendColors.includes(color)) {
                const inX = isBetween(prevX, 0, window.innerWidth) || isBetween(x, 0, window.innerWidth) || isBetween(0, prevX, x) || isBetween(window.innerWidth, prevX, x);
                const inY = isBetween(prevY, 0, window.innerHeight) || isBetween(y, 0, window.innerHeight) || isBetween(0, prevY, y) || isBetween(window.innerHeight, prevY, y);
                if (inX && inY) {
                    visibleLegendColors.push(color);
                }
            }

            prevX = x;
            prevY = y;
            prevVertical = vertical;
        }

        routeNames[color] = route["name"];
        if (!sortedColors.includes(color)) {
            sortedColors.push(color);
        }

        context.stroke();
    }
    sortedColors.sort();

    for (const blobKey in blobs) {
        const blob = blobs[blobKey];
        const xMin = blob["xMin"];
        const yMin = blob["yMin"];
        const xMax = blob["xMax"];
        const yMax = blob["yMax"];
        const shouldDraw = selectedColor === "" || blob["colors"].includes(selectedColor);

        beginDraw(context, shouldDraw ? "black" : "lightgray", 2);
        if (xMin === xMax && yMin === yMax) {
            context.arc(xMin, yMin, LINE_SIZE, 0, 2 * Math.PI);
        } else {
            context.arc(xMax, yMax, LINE_SIZE, 0, Math.PI / 2);
            context.lineTo(xMin, yMax + LINE_SIZE);
            context.arc(xMin, yMax, LINE_SIZE, Math.PI / 2, Math.PI);
            context.lineTo(xMin - LINE_SIZE, yMin);
            context.arc(xMin, yMin, LINE_SIZE, Math.PI, 3 * Math.PI / 2);
            context.lineTo(xMax, yMin - LINE_SIZE);
            context.arc(xMax, yMin, LINE_SIZE, 3 * Math.PI / 2, 2 * Math.PI);
            context.lineTo(xMax + LINE_SIZE, yMax);
        }
        context.fill();
        context.stroke();

        if (shouldDraw) {
            drawText(context, blob["name"], (xMin + xMax) / 2, yMax + LINE_SIZE * 2, "center", "top");
        }
    }

    let legendHtml = "";
    let resultsRoutesHtml = "";
    for (const colorIndex in sortedColors) {
        const color = sortedColors[colorIndex];
        legendHtml += getRouteHtml(color, routeNames[color], visibleLegendColors.includes(color), "", selectedColor === "" || selectedColor === color);
        resultsRoutesHtml += getRouteHtml(color, routeNames[color], true, color, selectedColor === "" || selectedColor === color);
    }

    let resultsStationsHtml = "";
    for (const stationId in stations) {
        resultsStationsHtml += getStationHtml(convertColor(stations[stationId]["color"]), stations[stationId]["name"], stationId);
    }

    const legendElement = document.getElementById("legend");
    if (visibleLegendColors.length === 0) {
        legendElement.setAttribute("hidden", "true");
    } else {
        legendElement.removeAttribute("hidden");
        legendElement.innerHTML = legendHtml;
        legendElement.style.maxHeight = window.innerHeight - 32 + "px";
    }
    document.getElementById("search_results_routes").innerHTML = resultsRoutesHtml;
    document.getElementById("search_results_stations").innerHTML = resultsStationsHtml;

    if (selectedStation === 0) {
        document.getElementById("station_info").style.display = "none";
    } else {
        const {name, color} = stations[selectedStation];
        const stationInfoElement = document.getElementById("station_info");
        stationInfoElement.style.display = "flex";

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

        let stationRoutesHtml = "";
        const addedRouteColors = [];
        for (const routeIndex in routes) {
            const route = routes[routeIndex];
            if (!addedRouteColors.includes(route["color"])) {
                for (const stationIndex in route["stations"]) {
                    if (route["stations"][stationIndex].split("_")[0] === selectedStation) {
                        stationRoutesHtml += getRouteHtml(convertColor(route["color"]), route["name"].replace("|", " "), true, "", true);
                        addedRouteColors.push(route["color"]);
                        break;
                    }
                }
            }
        }
        document.getElementById("station_routes").innerHTML = stationRoutesHtml;
        stationInfoElement.style.maxHeight = window.innerHeight - 80 + "px";
    }

    onSearch();
}

function beginDraw(context, strokeColor, lineWidth) {
    context.beginPath();
    context.strokeStyle = strokeColor;
    context.fillStyle = "white";
    context.lineWidth = lineWidth;
    context.lineJoin = "round";
}

function drawLine(context, x1, y1, vertical1, x2, y2, vertical2) {
    const differenceX = Math.abs(x2 - x1);
    const differenceY = Math.abs(y2 - y1);
    if (differenceX > differenceY) {
        if (vertical1 && vertical2) {
            const midpoint = (y1 + y2) / 2;
            context.lineTo(x1 + Math.sign(x2 - x1) * Math.abs(midpoint - y1), midpoint);
            context.lineTo(x2 - Math.sign(x2 - x1) * Math.abs(midpoint - y1), midpoint);
        } else if (!vertical1 && !vertical2) {
            context.lineTo(x1 + Math.sign(x2 - x1) * (differenceX - differenceY) / 2, y1);
            context.lineTo(x2 - Math.sign(x2 - x1) * (differenceX - differenceY) / 2, y2);
        } else if (vertical1) {
            context.lineTo(x1 + Math.sign(x2 - x1) * differenceY, y2);
        } else {
            context.lineTo(x2 - Math.sign(x2 - x1) * differenceY, y1);
        }
    } else {
        if (vertical1 && vertical2) {
            context.lineTo(x1, y1 + Math.sign(y2 - y1) * (differenceY - differenceX) / 2);
            context.lineTo(x2, y2 - Math.sign(y2 - y1) * (differenceY - differenceX) / 2);
        } else if (!vertical1 && !vertical2) {
            const midpoint = (x1 + x2) / 2;
            context.lineTo(midpoint, y1 + Math.sign(y2 - y1) * Math.abs(midpoint - x1));
            context.lineTo(midpoint, y2 - Math.sign(y2 - y1) * Math.abs(midpoint - x1));
        } else if (vertical1) {
            context.lineTo(x1, y2 - Math.sign(y2 - y1) * differenceX);
        } else {
            context.lineTo(x2, y1 + Math.sign(y2 - y1) * differenceX);
        }
    }
    context.lineTo(x2, y2);
}

function drawText(context, text, x, y, textAlign, textBaseline) {
    context.strokeStyle = "white";
    context.fillStyle = "black";
    context.textAlign = textAlign;
    context.textBaseline = textBaseline;
    const textSplit = text.split("|");
    let yStart = y;
    for (const index in textSplit) {
        const textPart = textSplit[index];
        const isTextCJK = isCJK(textPart);
        context.font = (isTextCJK ? 3 : 1.5) * LINE_SIZE + "px " + getComputedStyle(document.body).fontFamily;
        context.strokeText(textPart, x, yStart);
        context.fillText(textPart, x, yStart);
        yStart += (isTextCJK ? 3 : 1.5) * LINE_SIZE;
    }
}

function getStationHtml(color, name, id) {
    return "<div id='" + id + "' onclick='onClickStation(\"" + id + "\")' class='clickable' style='display: none'>" +
        "<span class='station' style='background: " + color + "'></span>" +
        "<span style='color: black'>" + name.replaceAll("|", " ") + "</span>" +
        "</div>";
}

function getRouteHtml(color, name, visible, id, showColor) {
    return "<div id='" + id + "' onclick='onClickLine(\"" + color + "\")' class='clickable' " + (visible ? "" : "style='display: none'") + ">" +
        "<span class='line' style='background: " + (showColor ? color : "lightgray") + "'></span>" +
        "<span style='color: " + (showColor ? "black" : "lightgray") + "'>" + name.replaceAll("|", " ") + "</span>" +
        "</div>";
}

function fetchArrivals() {
    if (selectedStation !== 0) {
        clearTimeout(fetchArrivalId);
        fetch(ARRIVALS_URL + "?worldIndex=" + dimension + "&stationId=" + selectedStation, {cache: "no-cache"}).then(response => response.json()).then(result => {
            arrivalData = result;
            fetchArrivalId = setTimeout(fetchArrivals, REFRESH_INTERVAL);
        });
    }
}

function refreshArrivals() {
    if (selectedStation !== 0) {
        clearTimeout(refreshArrivalId);
        let arrivalsHtml = "";
        for (const arrivalIndex in arrivalData) {
            const {arrival, destination} = arrivalData[arrivalIndex];
            const arrivalDifference = Math.floor((arrival - Date.now()) / 1000);
            const hour = Math.floor(arrivalDifference / 60);
            const minute = (arrivalDifference % 60).toString().padStart(2, "0");
            arrivalsHtml += "<div class='arrival'><span style='width: 100%; overflow-wrap: anywhere; white-space: normal'>" + destination.replace("|", " ") + "</span><span class='right_align'>" + (arrivalDifference < 0 ? "" : hour + ":" + minute) + "</span></div>";
        }
        document.getElementById("station_arrivals").innerHTML = arrivalsHtml;

        refreshArrivalId = setTimeout(refreshArrivals, 500);
    }
}

function onClickStation(id) {
    const {xMin, yMin, xMax, yMax} = json[dimension]["blobs"][id];
    canvasOffsetX += (xMin + xMax - window.innerWidth) / 2 / scale;
    canvasOffsetY += (yMin + yMax - window.innerHeight) / 2 / scale;

    selectedStation = id;
    onClearSearch();
    callback();
    fetchArrivals();
    refreshArrivals();
}

function onClickLine(color) {
    selectedColor = selectedColor === color ? "" : color;
    callback();
    onSearch();
}

function onSearch() {
    const searchBox = document.getElementById("search_box");
    const search = searchBox.value.toLowerCase();
    document.getElementById("clear_search_icon").innerText = search === "" ? "" : "clear";

    const {stations, routes} = json[dimension];

    const resultsStations = search === "" ? [] : Object.keys(stations).filter(station => stations[station]["name"].toLowerCase().includes(search));
    for (const stationId in stations) {
        document.getElementById(stationId).style.display = resultsStations.includes(stationId) ? "block" : "none";
    }

    const resultsRoutes = search === "" ? [] : Object.keys(routes).filter(route => routes[route]["name"].toLowerCase().includes(search));
    for (const routeIndex in routes) {
        const color = convertColor(routes[routeIndex]["color"]);
        document.getElementById(color).style.display = resultsRoutes.includes(routeIndex) ? "block" : "none";
    }

    const maxHeight = (window.innerHeight - 80) / 2;
    document.getElementById("search_results_stations").style.maxHeight = maxHeight + "px";
    document.getElementById("search_results_routes").style.maxHeight = maxHeight + "px";

    if (search !== "") {
        onClearStationInfo();
    }
}

function onClearSearch() {
    const searchBox = document.getElementById("search_box");
    searchBox.value = "";
    searchBox.focus();
    document.getElementById("clear_search_icon").innerText = "";
    clearSearchResults();
}

function onClearStationInfo() {
    selectedStation = 0;
    document.getElementById("station_info").style.display = "none";
}

function clearSearchResults() {
    const {stations, routes} = json[dimension];
    for (const stationId in stations) {
        document.getElementById(stationId).style.display = "none";
    }
    for (const routeIndex in routes) {
        const color = convertColor(routes[routeIndex]["color"]);
        document.getElementById(color).style.display = "none";
    }
}

function onCanvasMouseDown(event) {
    dragging = true;
    selecting = true;
    mouseClickX = event.screenX;
    mouseClickY = event.screenY;
}

function onCanvasMouseMove(event) {
    if (dragging) {
        canvasOffsetX += (mouseClickX - event.screenX) / scale;
        canvasOffsetY += (mouseClickY - event.screenY) / scale;
        onCanvasMouseDown(event);
        callback();
    }
    selecting = false;
}

function onCanvasMouseUp(event) {
    dragging = false;
    if (selecting) {
        const clickX = event.clientX;
        const clickY = event.clientY;
        const blobs = json[dimension]["blobs"];
        for (const stationId in blobs) {
            const {xMin, yMin, xMax, yMax} = json[dimension]["blobs"][stationId];
            const margin = LINE_SIZE * 5;
            if (isBetween(clickX, xMin - margin, xMax + margin) && isBetween(clickY, yMin - margin, yMax + margin)) {
                selectedStation = stationId;
                onClearSearch();
                callback();
                fetchArrivals();
                refreshArrivals();
                break;
            }
        }
    }
}

function onCanvasScroll(event) {
    const scrollAmount = event.deltaY < 0 ? 1 : -1;
    const oldScale = scale;
    if (oldScale > SCALE_LOWER_LIMIT && scrollAmount < 0) {
        canvasOffsetX -= (event.offsetX - window.innerWidth / 2) / scale;
        canvasOffsetY -= (event.offsetY - window.innerHeight / 2) / scale;
    }
    scale *= scrollAmount > 0 ? 2 : 0.5;
    scale = Math.min(SCALE_UPPER_LIMIT, Math.max(SCALE_LOWER_LIMIT, scale));
    if (oldScale < SCALE_UPPER_LIMIT && scrollAmount > 0) {
        canvasOffsetX += (event.offsetX - window.innerWidth / 2) / scale;
        canvasOffsetY += (event.offsetY - window.innerHeight / 2) / scale;
    }
    callback();
}
