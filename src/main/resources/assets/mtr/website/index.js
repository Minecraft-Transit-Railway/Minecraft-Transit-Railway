const URL = "http://localhost:8888/data";

const SCALE_UPPER_LIMIT = 64;
const SCALE_LOWER_LIMIT = 1 / 128;
const CIRCLE_RADIUS = 8;
const TEXT_PADDING = 4;
const FONT_SIZE = 12;

const CONVERT_X = x => (x - canvasOffsetX) * scale + window.innerWidth / 2;
const CONVERT_Y = y => (y - canvasOffsetY) * scale + window.innerHeight / 2;

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

        for (const routeKey in dimension["routes"]) {
            const route = dimension["routes"][routeKey];
            context.beginPath();
            context.strokeStyle = "#" + Number(route["color"]).toString(16).padStart(6, "0");
            context.lineWidth = 6;

            for (const stationIndex in route["stations"]) {
                const id = route["stations"][stationIndex];
                const position = positions[id];
                context.lineTo(CONVERT_X(position["x"]), CONVERT_Y(position["y"]));
                context.moveTo(CONVERT_X(position["x"]), CONVERT_Y(position["y"]));
            }

            context.stroke();
        }

        for (const blobKey in blobs) {
            const blob = blobs[blobKey];
            const xMin = CONVERT_X(blob["xMin"]);
            const yMin = CONVERT_Y(blob["yMin"]);
            const xMax = CONVERT_X(blob["xMax"]);
            const yMax = CONVERT_Y(blob["yMax"]);
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
