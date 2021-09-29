const URL = "http://localhost:8888/data";

const SCALE_UPPER_LIMIT = 64;
const SCALE_LOWER_LIMIT = 1 / 128;

const LINE_SIZE = 6;
const TEXT_PADDING = 6;

const convertX = x => Math.floor(x * scale / LINE_SIZE) * LINE_SIZE - canvasOffsetX * scale + window.innerWidth / 2;
const convertY = y => Math.floor(y * scale / LINE_SIZE) * LINE_SIZE - canvasOffsetY * scale + window.innerHeight / 2;
const convertColor = color => "#" + Number(color).toString(16).padStart(6, "0");
const isBetween = (x, a, b) => x >= Math.min(a, b) && x <= Math.max(a, b);

let json;
let dragging = false;
let mouseClickX = 0;
let mouseClickY = 0;
let canvasOffsetX = 0;
let canvasOffsetY = 0;
let scale = 1;
let selectedColor = "";
let dimension = 0;

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
setInterval(refresh, 5000);

function refresh() {
    fetch(URL, {cache: "no-cache"}).then(response => response.json()).then(result => {
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
    const blobs = data["blobs"];
    const positions = data["positions"];
    const stations = data["stations"];


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
    for (const routeKey in data["routes"]) {
        const route = data["routes"][routeKey];
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
        legendHtml += getRouteHtml(color, routeNames[color], visibleLegendColors.includes(color), "");
        resultsRoutesHtml += getRouteHtml(color, routeNames[color], true, color);
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
        const isCJK = textPart.match(/[\u3000-\u303f\u3040-\u309f\u30a0-\u30ff\uff00-\uff9f\u4e00-\u9faf\u3400-\u4dbf]/);
        context.font = (isCJK ? 3 : 1.5) * LINE_SIZE + "px " + getComputedStyle(document.body).fontFamily;
        context.strokeText(textPart, x, yStart);
        context.fillText(textPart, x, yStart);
        yStart += (isCJK ? 3 : 1.5) * LINE_SIZE;
    }
}

function getStationHtml(color, name, id) {
    return "<div id='" + id + "' onclick='onClickStation(\"" + id + "\")' class='clickable' style='display: none'>" +
        "<span class='station' style='background: " + color + "'></span>" +
        "<span style='color: black'>" + name.replaceAll("|", " ") + "</span>" +
        "</div>";
}

function getRouteHtml(color, name, visible, id) {
    return "<div id='" + id + "' onclick='onClickLine(\"" + color + "\")' class='clickable' " + (visible ? "" : "style='display: none'") + ">" +
        "<span class='line' style='background: " + (selectedColor === "" || selectedColor === color ? color : "lightgray") + "'></span>" +
        "<span style='color: " + (selectedColor === "" || selectedColor === color ? "black" : "lightgray") + "'>" + name.replaceAll("|", " ") + "</span>" +
        "</div>";
}

function onClickStation(id) {
    const {xMin, yMin, xMax, yMax} = json[dimension]["blobs"][id];
    canvasOffsetX += (xMin + xMax - window.innerWidth) / 2 / scale;
    canvasOffsetY += (yMin + yMax - window.innerHeight) / 2 / scale;
    callback();
    onSearch();
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

    const maxHeight = window.innerHeight / 3;
    document.getElementById("search_results_stations").style.maxHeight = maxHeight + "px";
    document.getElementById("search_results_routes").style.maxHeight = maxHeight + "px";
}

function onClearSearch() {
    const searchBox = document.getElementById("search_box");
    searchBox.value = "";
    searchBox.focus();
    document.getElementById("clear_search_icon").innerText = "";
    document.getElementById("search_results_stations").innerHTML = "";
    document.getElementById("search_results_routes").innerHTML = "";
}

function onCanvasMouseDown(event) {
    dragging = true;
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
}

function onCanvasMouseUp() {
    dragging = false;
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
