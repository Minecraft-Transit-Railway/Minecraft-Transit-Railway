import DRAWING from "./drawing.js"
import FetchData from "./fetch.js";
import UTILITIES from "./utilities.js";
import DATA from "./data.js";

const DATA_REFRESH_INTERVAL = 30000;
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
		const fetchData = new FetchData(() => {
			const [lat, lon, latSpan, lonSpan] = DRAWING.getCenterLatLon();
			return `https://api.pugetsound.onebusaway.org/api/where/trips-for-location.json?key=TEST&lat=${lat / ZOOM_OFFSET + LAT_OFFSET}&lon=${lon / ZOOM_OFFSET + LON_OFFSET}&latSpan=${Math.max(latSpan / ZOOM_OFFSET, 0.1)}&lonSpan=${Math.max(lonSpan / ZOOM_OFFSET, 0.1)}&includeSchedule=true&includeReferences=true`;
		}, DATA_REFRESH_INTERVAL, false, () => true, result => DATA.parseOBA(result, LAT_OFFSET, LON_OFFSET, ZOOM_OFFSET));
		// const fetchData = new FetchData(() => "data2.json", DATA_REFRESH_INTERVAL, false, () => true, result => DATA.parseMTR(result));
		fetchData.fetchData();
	}
};
Promise.all(UTILITIES.fonts.map(font => new FontFaceObserver(font).load())).then(init, init);
