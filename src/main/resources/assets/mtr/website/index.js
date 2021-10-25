const URL = document.location.origin + document.location.pathname;
const DATA_URL = URL + "data"
const ARRIVALS_URL = URL + "arrivals";
const REFRESH_DATA_INTERVAL = 60000;
const REFRESH_ARRIVALS_INTERVAL = 5000;

const LINE_SIZE = 6;
const TEXT_PADDING = 6;
const MAX_ARRIVALS = 5;
const FILTER = new PIXI.filters.BlurFilter();

let dimension = 0; // TODO other dimensions
let selectedColor = -1;
let selectedStation = 0;

(function() {
    window.onload = () => {

        const isSafari = window.safari !== undefined;
        if(isSafari){
            PIXI.settings.PREFER_ENV = PIXI.ENV.WEBGL;
        }

        const app = new PIXI.Application({autoResize: true, antialias: true});
        const searchBoxElement = document.getElementById("search_box");

        let json;
        let refreshDataId = 0;

        searchBoxElement.onchange = () => onSearch(json[dimension]);
        searchBoxElement.onpaste = () => onSearch(json[dimension]);
        searchBoxElement.oninput = () => onSearch(json[dimension]);

        document.getElementById("zoom_in_icon").onclick = () => {
            onZoom(-1, window.innerWidth / 2, window.innerHeight / 2, container);
            drawMap(container, json[dimension]);
        };
        document.getElementById("zoom_out_icon").onclick = () => {
            onZoom(1, window.innerWidth / 2, window.innerHeight / 2, container);
            drawMap(container, json[dimension]);
        };
        document.getElementById("clear_search_icon").onclick = () => onClearSearch(json[dimension], true);

        window.addEventListener("resize", resize);
        const background = new PIXI.Sprite(PIXI.Texture.WHITE);
        resize();

        background.tint = getColorStyle("--backgroundColor");
        background.interactive = true;
        background.on("pointerdown", onCanvasMouseDown);
        background.on("pointermove", event => onCanvasMouseMove(event, container));
        background.on("pointerup", onCanvasMouseUp);
        app.stage.addChild(background);

        const container = new PIXI.Container();
        app.stage.addChild(container);

        document.body.appendChild(app.view);
        app.view.addEventListener("wheel", event => {
            onCanvasScroll(event, container);
            drawMap(container, json[dimension]);
        });
        app.ticker.add(delta => update(delta, container));

        fetchMainData();

        function fetchMainData() {
            clearTimeout(refreshDataId);
            fetch(DATA_URL, {cache: "no-cache"}).then(response => response.json()).then(result => {
                json = result;
                drawMap(container, json[dimension]);
                refreshDataId = setTimeout(fetchMainData, REFRESH_DATA_INTERVAL);
            });
        }

        function resize() {
            app.renderer.resize(window.innerWidth, window.innerHeight);
            background.width = app.screen.width;
            background.height = app.screen.height;
            document.getElementById("search").style.maxWidth = window.innerWidth - 32 + "px";
            document.getElementById("station_info").style.maxHeight = window.innerHeight - 80 + "px";
        }
    }
})();

function onSearch(data) {
	const searchBox = document.getElementById("search_box");
	const search = searchBox.value.toLowerCase();
	document.getElementById("clear_search_icon").innerText = search === "" ? "" : "clear";

	const {stations, routes} = data;

	const resultsStations = search === "" ? [] : Object.keys(stations).filter(station => stations[station]["name"].toLowerCase().includes(search));
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
		onClearStationInfo();
	}
}

function onClearStationInfo() {
	selectedStation = 0;
	document.getElementById("station_info").style.display = "none";
}

function onClearSearch(data, focus) {
	const searchBox = document.getElementById("search_box");
	searchBox.value = "";
	if (focus) {
		searchBox.focus();
	}
	document.getElementById("clear_search_icon").innerText = "";
	const {stations, routes} = data;
	for (const stationId in stations) {
		document.getElementById(stationId).style.display = "none";
	}
	for (const index in routes) {
		document.getElementById(routes[index]["color"]).style.display = "none";
	}
}
