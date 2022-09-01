import DRAWING from "./drawing.js"
import FetchData from "./fetch.js";
import UTILITIES from "./utilities.js";
import DATA from "./data.js";
import SETTINGS from "./settings.js";

const DATA_REFRESH_INTERVAL = 30000;
const INFO_REFRESH_INTERVAL = 5000;
const LAT_OFFSET = 47.653435;
const LON_OFFSET = -122.305641;
const ZOOM_OFFSET = 100000;

const init = () => {
	if (UTILITIES.testMode) {
		let middleAngle = 0;
		let outerAngles = 0;
		const drawTestStations = () => {
			DRAWING.drawTest(6, middleAngle, outerAngles);
			outerAngles += 45;
			if (outerAngles >= 180) {
				outerAngles = 0;
				middleAngle += 45;
				if (middleAngle >= 180) {
					middleAngle = 0;
				}
			}
		};
		setInterval(drawTestStations, 1000);
		drawTestStations();
	} else {
		if (UTILITIES.obaMode) {
			new FetchData(() => {
				const [lat, lon, latSpan, lonSpan] = DRAWING.getCenterLatLon();
				return `https://api.pugetsound.onebusaway.org/api/where/trips-for-location.json?key=TEST&lat=${lat / ZOOM_OFFSET + LAT_OFFSET}&lon=${lon / ZOOM_OFFSET + LON_OFFSET}&latSpan=${Math.max(latSpan / ZOOM_OFFSET, 0.1)}&lonSpan=${Math.max(lonSpan / ZOOM_OFFSET, 0.1)}&includeSchedule=true&includeReferences=true`;
			}, DATA_REFRESH_INTERVAL, false, () => true, result => DATA.parseOBA(result, LAT_OFFSET, LON_OFFSET, ZOOM_OFFSET)).fetchData();
		} else {
			new FetchData(() => SETTINGS.url + "data", DATA_REFRESH_INTERVAL, false, () => true, result => DATA.parseMTR(result)).fetchData();
			new FetchData(() => SETTINGS.url + "info", INFO_REFRESH_INTERVAL, false, () => true, result => {
				const playerListElement = document.getElementById("player_list");
				playerListElement.innerText = "";

				for (let i = 0; i < result.length; i++) {
					result[i].forEach(playerData => {
						const {player, name, number, destination, circular, color} = playerData;
						const noRoute = name === "" && destination === "";
						playerListElement.innerHTML +=
							`<div class="player text">` +
							`<img src="https://mc-heads.net/avatar/${player}" alt=""/>` +
							`<span style="${noRoute ? "" : "max-width: 6em; min-width: 6em; overflow: hidden"}">&nbsp;&nbsp;&nbsp;${player}</span>` +
							`<div class="player_route" style="display: ${noRoute ? "none" : ""}; border-left: 0.3em solid ${UTILITIES.convertColor(color)}">` +
							`<div class="arrival">` +
							`<span class="arrival_text">&nbsp;${number.replace(/\|/g, " ")} ${name.split("||")[0].replace(/\|/g, " ")}</span>` +
							`</div>` +
							`<div class="arrival">` +
							`<span class="material-icons small">${circular === "" ? "chevron_right" : circular === "cw" ? "rotate_right" : "rotate_left"}</span>` +
							`<span class="arrival_text">${destination.replace(/\|/g, " ")}</span>` +
							`</div>` +
							`</div>` +
							`</div>`;
					});

					if (result[i].length > 0) {
						playerListElement.innerHTML += `<div class="spacer"></div>`;
					}
				}

				document.getElementById("settings").style.maxHeight = window.innerHeight - 80 + "px";
			}).fetchData();
		}
	}
};
Promise.all(UTILITIES.fonts.map(font => new FontFaceObserver(font).load())).then(init, init);
