export default class FetchData {

	#url;
	#refreshInterval;
	#scheduleCallback;
	#condition;
	#callback;
	#fetchId;
	#refreshId;
	#useUtf16;

	constructor(url, refreshInterval, scheduleCallback, condition, callback, useUtf16) {
		this.#url = url;
		this.#refreshInterval = refreshInterval;
		this.#scheduleCallback = scheduleCallback;
		this.#condition = condition;
		this.#callback = callback;
		this.#fetchId = 0;
		this.#refreshId = 0;
		this.#useUtf16 = useUtf16;
	}

	fetchData(args) {
		if (this.#condition()) {
			clearTimeout(this.#fetchId);
			fetch(this.#url(), {cache: "no-cache"}).then(response => response.arrayBuffer()).then(result => {
				this.#fetchId = setTimeout(() => this.fetchData(args), this.#refreshInterval);
				this.#refreshData(JSON.parse(new TextDecoder(this.#useUtf16 ? "utf-16be" : "utf-8").decode(result)), args);
			}).catch(() => this.#fetchId = setTimeout(() => this.fetchData(args), this.#refreshInterval));
		}
	}

	#refreshData(result, args) {
		if (this.#condition()) {
			clearTimeout(this.#refreshId);
			this.#callback(result, args);
			if (this.#scheduleCallback) {
				this.#refreshId = setTimeout(() => this.#refreshData(result, args), 1000);
			}
		}
	}
}
