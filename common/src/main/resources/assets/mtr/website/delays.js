import FetchData from "./fetch.js";
import SETTINGS from "./index.js";
import CANVAS from "./utilities.js";

const REFRESH_INTERVAL = 5000;
const FETCH_DATA = new FetchData(() => SETTINGS.delaysUrl, REFRESH_INTERVAL, true, () => document.getElementById("delays").style.display === "block", result => {
	const data = result[SETTINGS.dimension];
	data.sort((a, b) => b["delay"] - a["delay"]);
	const resultElement = document.getElementById("delays_result");
	resultElement.innerText = "";

	for (const {name, destination, color, delay, time, x, y, z} of data) {
		const currentMillis = Date.now();
		const nameSplit = name.split("||")[0].split("|");
		const destinationSplit = destination.split("|");
		const scale = 0.9;

		const spacerElement = document.createElement("div");
		spacerElement.className = "spacer";
		resultElement.appendChild(spacerElement);

		const innerElement = document.createElement("div");
		innerElement.className = "delay_outer";
		innerElement.innerHTML =
			`<div class="delay" style="width: ${scale * 100}%">` +
			`<div class="arrival">` +
			`<span class="arrival_text" style="width: 50%">${nameSplit[Math.floor(currentMillis / 3000) % nameSplit.length]}</span>` +
			`<span class="arrival_text material-icons tight" style="width: ${5 / scale}%">schedule</span>` +
			`<span class="arrival_text" style="width: ${50 - 5 / scale}%">${CANVAS.formatTime(delay / 20)}</span>` +
			`</div>` +
			`<div class="arrival">` +
			`<span class="arrival_text material-icons tight" style="width: ${5 / scale}%">chevron_right</span>` +
			`<span class="arrival_text" style="width: ${50 - 5 / scale}%">${destinationSplit[Math.floor(currentMillis / 3000) % destinationSplit.length]}</span>` +
			`<span class="arrival_text material-icons tight" style="width: ${5 / scale}%">my_location</span>` +
			`<span class="arrival_text right_align" style="width: ${50 - 5 / scale}%">(${x}, ${y}, ${z})</span>` +
			`</div>` +
			`</div>`;

		const copyElement = document.createElement("span");
		copyElement.className = "arrival_text right_align material-icons clickable";
		copyElement.innerText = "content_copy";
		copyElement.onclick = event => {
			navigator.clipboard.writeText(`/tp ${x} ${y} ${z}`);
			event.target.innerText = "check";
			setTimeout(() => event.target.innerText = "content_copy", 1000);
		};
		innerElement.appendChild(copyElement);

		resultElement.appendChild(innerElement);
	}

	resultElement.style.maxHeight = window.innerHeight - 80 + "px";
});

document.getElementById("delays_icon").onclick = () => {
	SETTINGS.clearPanes();
	document.getElementById("delays").style.display = "block";
	document.getElementById("delays_result").innerHTML =
		`<div class="spacer"></div>` +
		`<div class="info_center"><span class="material-icons large">refresh</span></div>`;
	FETCH_DATA.fetchData();
};
