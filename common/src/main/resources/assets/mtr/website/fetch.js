export default class FetchData {

	#url;
	#refreshInterval;
	#scheduleCallback;
	#condition;
	#callback;
	#fetchId;
	#refreshId;

	constructor(url, refreshInterval, scheduleCallback, condition, callback) {
		this.#url = url;
		this.#refreshInterval = refreshInterval;
		this.#scheduleCallback = scheduleCallback;
		this.#condition = condition;
		this.#callback = callback;
		this.#fetchId = 0;
		this.#refreshId = 0;
	}

	fetchData(args) {
		if (this.#condition()) {
			clearTimeout(this.#fetchId);
			fetch(this.#url(), {cache: "no-cache"}).then(response => response.json()).then(result => {
				this.#fetchId = setTimeout(() => this.fetchData(args), this.#refreshInterval);
				this.#refreshData(result, args);
			});
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
