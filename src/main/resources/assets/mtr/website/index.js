const URL = "http://localhost:8888/data";

const SCALE_UPPER_LIMIT = 64;
const SCALE_LOWER_LIMIT = 1 / 128;
const CIRCLE_RADIUS = 8;
const LINE_WIDTH = 6;
const LEGEND_LINE_LENGTH = 48;
const TEXT_PADDING = 4;
const LEGEND_PADDING = 16;
const FONT_SIZE = 12;

const convertX = x => (x - canvasOffsetX) * scale + window.innerWidth / 2;
const convertY = y => (y - canvasOffsetY) * scale + window.innerHeight / 2;
const convertColor = color => "#" + Number(color).toString(16).padStart(6, "0");

let json;
let dragging = false;
let mouseClickX = 0;
let mouseClickY = 0;
let canvasOffsetX = 0;
let canvasOffsetY = 0;
let scale = 1;

refresh();
setInterval(refresh, 1000);

function refresh() {
    fetch(URL, {cache: "no-cache"}).then(response => response.json()).then(result => {
        json = result;

        for (const dimensionIndex in json) {
            const dimension = json[dimensionIndex];
            const positions = dimension["positions"];
            dimension["blobs"] = {};
            for (const positionKey in positions) {
                const stationId = positionKey.split("_")[0];
                const currentX = positions[positionKey]["x"];
                const currentY = positions[positionKey]["y"];
                let blob = dimension["blobs"][stationId];
                if (blob === undefined) {
                    dimension["blobs"][stationId] = {
                        xMin: currentX,
                        yMin: currentY,
                        xMax: currentX,
                        yMax: currentY,
                        name: dimension["stations"][stationId]["name"],
                    };
                } else {
                    blob["xMin"] = Math.min(blob["xMin"], currentX);
                    blob["yMin"] = Math.min(blob["yMin"], currentY);
                    blob["xMax"] = Math.max(blob["xMax"], currentX);
                    blob["yMax"] = Math.max(blob["yMax"], currentY);
                }
            }
        }

        callback();
    });
}

function callback() {
    const canvas = document.getElementById("map");
    canvas.addEventListener("mousedown", onCanvasMouseDown);
    canvas.addEventListener("mousemove", onCanvasMouseMove);
    canvas.addEventListener("mouseup", onCanvasMouseUp);
    canvas.addEventListener("wheel", onCanvasScroll);

    const context = canvas.getContext("2d");
    context.canvas.width = window.innerWidth;
    context.canvas.height = window.innerHeight;

    for (const dimensionIndex in json) {
        const dimension = json[dimensionIndex];
        const positions = dimension["positions"];
        const blobs = dimension["blobs"];
        const legend = {};

        for (const routeKey in dimension["routes"]) {
            const route = dimension["routes"][routeKey];
            const color = convertColor(route["color"]);

            if (legend[color] === undefined) {
                legend[color] = route["name"].split("||")[0].replace("|", " ");
            }

            context.beginPath();
            context.strokeStyle = color;
            context.lineWidth = LINE_WIDTH;
            context.lineJoin = "round";

            let prevX = undefined;
            let prevY = undefined;
            for (const stationIndex in route["stations"]) {
                const id = route["stations"][stationIndex];
                const x = positions[id]["x"];
                const y = positions[id]["y"];
                if (prevX === undefined || prevY === undefined) {
                    context.moveTo(convertX(x), convertY(y));
                } else {
                    drawLine(context, prevX, prevY, x, y);
                }
                prevX = x;
                prevY = y;
            }

            context.stroke();
        }

        for (const blobKey in blobs) {
            const blob = blobs[blobKey];
            const xMin = convertX(blob["xMin"]);
            const yMin = convertY(blob["yMin"]);
            const xMax = convertX(blob["xMax"]);
            const yMax = convertY(blob["yMax"]);
            context.beginPath();
            context.strokeStyle = "black";
            context.fillStyle = "white";
            context.lineWidth = 2;
            if (xMin === xMax && yMin === yMax) {
                context.arc(xMin, yMin, CIRCLE_RADIUS, 0, 2 * Math.PI);
            } else {
                context.arc(xMax, yMax, CIRCLE_RADIUS, 0, Math.PI / 2);
                context.lineTo(xMin, yMax + CIRCLE_RADIUS);
                context.arc(xMin, yMax, CIRCLE_RADIUS, Math.PI / 2, Math.PI);
                context.lineTo(xMin - CIRCLE_RADIUS, yMin);
                context.arc(xMin, yMin, CIRCLE_RADIUS, Math.PI, 3 * Math.PI / 2);
                context.lineTo(xMax, yMin - CIRCLE_RADIUS);
                context.arc(xMax, yMin, CIRCLE_RADIUS, 3 * Math.PI / 2, 2 * Math.PI);
                context.lineTo(xMax + CIRCLE_RADIUS, yMax);
            }
            context.fill();
            context.stroke();

            context.strokeStyle = "white";
            context.fillStyle = "black";
            context.font = "bold " + FONT_SIZE + "px Arial";
            context.textAlign = "center";
            context.textBaseline = "top";
            const nameSplit = blob["name"].split("|");
            let y = yMax + CIRCLE_RADIUS + TEXT_PADDING;
            for (const index in nameSplit) {
                context.strokeText(nameSplit[index], (xMin + xMax) / 2, y);
                context.fillText(nameSplit[index], (xMin + xMax) / 2, y);
                y += FONT_SIZE;
            }
        }

        const sortedColors = [];
        let textWidth = 0;
        for (const color in legend) {
            sortedColors.push(color);
            textWidth = Math.max(context.measureText(legend[color]).width, textWidth);
        }
        sortedColors.sort();
        const legendHeight = sortedColors.length * (FONT_SIZE + TEXT_PADDING) + LEGEND_PADDING * 2 - (sortedColors.length > 0 ? TEXT_PADDING : 0);
        const legendStart = window.innerHeight - legendHeight;

        context.beginPath();
        context.strokeStyle = "lightgray";
        context.fillStyle = "white";
        context.lineWidth = 0;
        context.rect(-LEGEND_PADDING, legendStart, LEGEND_PADDING * 4 + LEGEND_LINE_LENGTH + textWidth, legendHeight + LEGEND_PADDING * 3);
        context.fill();
        context.stroke();

        let y = legendStart + FONT_SIZE / 2 + LEGEND_PADDING;
        for (const colorIndex in sortedColors) {
            context.beginPath();
            context.lineWidth = LINE_WIDTH;
            context.strokeStyle = sortedColors[colorIndex];
            context.moveTo(LEGEND_PADDING, y);
            context.lineTo(LEGEND_PADDING + LEGEND_LINE_LENGTH, y);
            context.stroke();

            context.fillStyle = "black";
            context.font = "bold " + FONT_SIZE + "px Arial";
            context.textAlign = "left";
            context.textBaseline = "middle";
            context.fillText(legend[sortedColors[colorIndex]], LEGEND_PADDING * 2 + LEGEND_LINE_LENGTH, y);

            y += FONT_SIZE + TEXT_PADDING;
        }
    }
}

function drawLine(context, x1, y1, x2, y2) {
    const differenceX = Math.abs(x2 - x1);
    const differenceY = Math.abs(y2 - y1);
    if (differenceX > differenceY) {
        const sign = Math.sign(x2 - x1);
        if (y1 > y2) {
            context.lineTo(convertX(x2 - sign * differenceY), convertY(y1));
        } else {
            context.lineTo(convertX(x1 + sign * differenceY), convertY(y2));
        }
        context.lineTo(convertX(x2), convertY(y2));
    } else {
        const sign = Math.sign(y2 - y1);
        if (x1 > x2) {
            context.lineTo(convertX(x1), convertY(y2 - sign * differenceX));
        } else {
            context.lineTo(convertX(x2), convertY(y1 + sign * differenceX));
        }
        context.lineTo(convertX(x2), convertY(y2));
    }
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
