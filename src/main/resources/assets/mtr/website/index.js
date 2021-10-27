import CANVAS from "./utilities.js"
import drawMap from "./drawing.js";

const URL = document.location.origin + document.location.pathname;
const SETTINGS = {
	dataUrl: URL + "data",
	arrivalsUrl: URL + "arrivals",
	refreshDataInterval: 60000,
	refreshArrivalsInterval: 5000,
	lineSize: 6,
	dimension: 0, // TODO other dimensions
	isCJK: text => text.match(/[\u3000-\u303f\u3040-\u309f\u30a0-\u30ff\uff00-\uff9f\u4e00-\u9faf\u3400-\u4dbf]/),
	getColorStyle: style => parseInt(getComputedStyle(document.body).getPropertyValue(style).replace(/#/g, ""), 16),
};

const fetchMainData = () => {
	clearTimeout(refreshDataId);
	fetch(SETTINGS.dataUrl, {cache: "no-cache"}).then(response => response.json()).then(result => {
		json = result;
		drawMap(container, json[SETTINGS.dimension]);
		refreshDataId = setTimeout(fetchMainData, SETTINGS.refreshDataInterval);
	});
}

const resize = () => {
	app.renderer.resize(window.innerWidth, window.innerHeight);
	background.width = app.screen.width;
	background.height = app.screen.height;
	document.getElementById("search").style.maxWidth = window.innerWidth - 32 + "px";
	document.getElementById("station_info").style.maxHeight = window.innerHeight - 80 + "px";
}

if (window.safari !== undefined) {
	PIXI.settings.PREFER_ENV = PIXI.ENV.WEBGL;
}

const app = new PIXI.Application({autoResize: true, antialias: true});

let json;
let refreshDataId = 0;

document.getElementById("zoom_in_icon").onclick = () => {
	CANVAS.onZoom(-1, window.innerWidth / 2, window.innerHeight / 2, container);
	drawMap(container, json[SETTINGS.dimension]);
};
document.getElementById("zoom_out_icon").onclick = () => {
	CANVAS.onZoom(1, window.innerWidth / 2, window.innerHeight / 2, container);
	drawMap(container, json[SETTINGS.dimension]);
};
document.getElementById("clear_search_icon").onclick = () => onClearSearch(json[SETTINGS.dimension], true);

window.addEventListener("resize", resize);
const background = new PIXI.Sprite(PIXI.Texture.WHITE);
resize();

background.tint = SETTINGS.getColorStyle("--backgroundColor");
background.interactive = true;
background.on("pointerdown", CANVAS.onCanvasMouseDown);
background.on("pointermove", event => CANVAS.onCanvasMouseMove(event, container));
background.on("pointerup", CANVAS.onCanvasMouseUp);
app.stage.addChild(background);

const container = new PIXI.Container();
app.stage.addChild(container);

document.body.appendChild(app.view);
app.view.addEventListener("wheel", event => {
	CANVAS.onCanvasScroll(event, container);
	drawMap(container, json[SETTINGS.dimension]);
});
app.ticker.add(delta => CANVAS.update(delta, container));

fetchMainData();

export default SETTINGS;
