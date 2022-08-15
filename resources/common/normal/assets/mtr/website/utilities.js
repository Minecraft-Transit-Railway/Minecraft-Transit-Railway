const TAN_22_5 = Math.tan(Math.PI / 8);
const UTILITIES = {
	isBetween: (x, a, b) => x >= Math.min(a, b) && x < Math.max(a, b),
	runForTime: (object, callback, onComplete) => {
		const time = Date.now();
		for (const key in object) {
			callback(key);
			if (Date.now() - time > 20) {
				setTimeout(() => UTILITIES.runForTime(object, callback, onComplete));
				return;
			}
		}
		if (onComplete != null) {
			setTimeout(onComplete);
		}
	},
	connectLine: (x1, y1, direction1, offsetIndex1, routeCount1, x2, y2, direction2, offsetIndex2, routeCount2, lineWidth, segments) => {
		const offset1 = (offsetIndex1 - (routeCount1 - 1) / 2) * lineWidth;
		const offset2 = (offsetIndex2 - (routeCount2 - 1) / 2) * lineWidth;
		const offset1Rotated = rotatePoint(offset1, 0, direction1);
		const offset2Rotated = rotatePoint(offset2, 0, direction2);
		x1 += offset1Rotated["x"];
		y1 += offset1Rotated["y"];
		x2 += offset2Rotated["x"];
		y2 += offset2Rotated["y"];

		const {x, y} = rotatePoint(x2 - x1, y2 - y1, -direction1);
		const signX = Math.sign(x);
		const signY = Math.sign(y);
		const absX = Math.abs(x);
		const absY = Math.abs(y);
		const rotatedDirection = (direction2 - direction1 + 180) % 180;
		const isTopRight = (x > 0) === (y > 0);
		const points = [];

		points.push(rotatePoint(0, 0, direction1));
		const getEndOffset = (maxHeight, isTop) => clamp(signX * offset1 * TAN_22_5 + routeCount1 * lineWidth / (isTop ? 2 : -2), maxHeight);

		if (rotatedDirection === 0) {
			if (absX > absY) {
				const difference = absY / 2;
				const lineOffset = clamp(offset1, absY / routeCount1);
				const endOffset1 = getEndOffset((difference - Math.abs(lineOffset)) / 2, false);
				const endOffset2 = getEndOffset((difference - Math.abs(lineOffset)) / 2, true);
				points.push(rotatePoint(0, -signY * endOffset1, direction1));
				points.push(rotatePoint(signX * difference + signX * endOffset1 - lineOffset, signY * difference + (isTopRight ? -1 : 1) * lineOffset, direction1));
				points.push(rotatePoint(x - signX * difference + signX * endOffset2 - lineOffset, signY * difference + (isTopRight ? -1 : 1) * lineOffset, direction1));
				points.push(rotatePoint(x, y - signY * endOffset2, direction1));
			} else {
				const difference = (absY - absX) / 2;
				const lineOffset = clamp((isTopRight ? -1 : 1) * offset1 * TAN_22_5, difference);
				points.push(rotatePoint(0, signY * difference + lineOffset, direction1));
				points.push(rotatePoint(x, y - signY * difference + lineOffset, direction1));
			}
		} else {
			const var2 = direction1 === 45 && direction2 === 0 || direction1 === 90 && direction2 === 45 || direction1 === 135 && direction2 === 0 || direction1 === 135 && direction2 === 90;
			const getFinalPoint = () => rotatePoint(0, ((rotatedDirection === 45) !== var2 ? -1 : 1) * getEndOffset(Math.sqrt(absX * absX / 4 + absY * absY / 4), (rotatedDirection === 45) !== var2), rotatedDirection);
			if (absX > absY) {
				const endOffset1 = getEndOffset(absY / 2, false);
				points.push(rotatePoint(0, -signY * endOffset1, direction1));
				if (rotatedDirection === 90) {
					points.push(rotatePoint(signX * absY + signX * endOffset1, y, direction1));
				} else {
					const finalPoint = getFinalPoint();
					points.push(rotatePoint(signX * absY + signX * endOffset1 + (rotatedDirection === 45 === isTopRight ? 1 : -1) * signX * finalPoint["x"], y - signX * finalPoint["y"], direction1));
					points.push(rotatePoint(x - signX * finalPoint["x"], y - signX * finalPoint["y"], direction1));
				}
			} else {
				if (rotatedDirection === 90) {
					const var1 = direction1 - direction2 !== 90;
					const endOffset2 = getEndOffset(absX / 2, isTopRight === var1);
					points.push(rotatePoint(0, y - signY * absX + (var1 ? 1 : -1) * signX * endOffset2, direction1));
					points.push(rotatePoint(x - (var1 ? 1 : -1) * signY * endOffset2, y, direction1));
				} else {
					if (rotatedDirection === 45 && isTopRight || rotatedDirection === 135 && !isTopRight) {
						const endOffset1 = getEndOffset(absY / 2, false);
						points.push(rotatePoint(0, -signY * endOffset1, direction1));
						const finalPoint = getFinalPoint();
						points.push(rotatePoint(x + signX * finalPoint["x"], signY * absX - signY * endOffset1 - signX * finalPoint["y"], direction1));
						points.push(rotatePoint(x + signX * finalPoint["x"], y + signX * finalPoint["y"], direction1));
					} else {
						points.push(rotatePoint(0, y - signY * absX + signX, direction1));
					}
				}
			}
		}

		points.push(rotatePoint(x, y, direction1));
		points.forEach(point => segments.push({"x": point["x"] + x1, "y": point["y"] + y1}));
	},
	onClearSearch: (data, focus) => {
		const searchBox = document.getElementById("search_box");
		searchBox.value = "";
		if (focus) {
			searchBox.focus();
		}
		document.getElementById("clear_search_icon").style.display = "none";
		// const {stations, routes} = data;
		// for (const stationId in stations) {
		// 	document.getElementById(stationId).style.display = "none";
		// }
		// for (const index in routes) {
		// 	document.getElementById(routes[index]["color"]).style.display = "none";
		// }
	},
	clearPanes: () => {
		UTILITIES.selectedStation = 0;
		UTILITIES.selectedRoutes = [];
		UTILITIES.selectedDirectionsStations = [];
		UTILITIES.selectedDirectionsSegments = {};
		showSettings = false;
		document.getElementById("station_info").style.display = "none";
		document.getElementById("route_info").style.display = "none";
		document.getElementById("directions").style.display = "none";
		document.getElementById("settings").style.display = "none";
	},
	convertGtfsRouteType: routeType => {
		switch (routeType) {
			case 0:
			case 5:
			case 12:
				return UTILITIES.routeTypes[1];
			case 2:
				return UTILITIES.routeTypes[2];
			case 3:
			case 11:
				return UTILITIES.routeTypes[7];
			case 4:
				return UTILITIES.routeTypes[4];
			case 6:
				return UTILITIES.routeTypes[6];
			default:
				return UTILITIES.routeTypes[0];
		}
	},
	directionToAngle: direction => {
		const directionIndex = ["N", "NE", "E", "SE", "S", "SW", "W", "NW"].indexOf(direction);
		return UTILITIES.angles[(directionIndex >= 0 ? directionIndex : 0) % UTILITIES.angles.length];
	},
	convertColor: colorInt => "#" + Number(colorInt).toString(16).padStart(6, "0"),
	isCJK: text => text.match(/[\u3000-\u303f\u3040-\u309f\u30a0-\u30ff\uff00-\uff9f\u4e00-\u9faf\u3400-\u4dbf]/),
	routeTypes: {
		"train_normal": "directions_train",
		"train_light_rail": "tram",
		"train_high_speed": "train",
		"boat_normal": "sailing",
		"boat_light_rail": "directions_boat",
		"boat_high_speed": "snowmobile",
		"cable_car_normal": "airline_seat_recline_extra",
		"bus_normal": "directions_bus",
		"bus_light_rail": "local_taxi",
		"bus_high_speed": "airport_shuttle",
	},
	angles: [0, 45, 90, 135],
	fonts: ["Noto Sans", "Noto Serif TC", "Noto Serif SC", "Noto Serif JP", "Noto Serif KR", "Material Icons"],
	obaMode: false,
	testMode: false,
};

const rotatePoint = (x, y, direction) => {
	const getSin = direction => {
		switch ((direction + 360) % 360) {
			case 90:
				return 1;
			case 270:
				return -1;
			case 45:
			case 135:
				return Math.SQRT1_2;
			case 225:
			case 315:
				return -Math.SQRT1_2;
			default:
				return 0;
		}
	};
	const getCos = direction => {
		switch ((direction + 360) % 360) {
			case 0:
				return 1;
			case 180:
				return -1;
			case 45:
			case 315:
				return Math.SQRT1_2;
			case 135:
			case 225:
				return -Math.SQRT1_2;
			default:
				return 0;
		}
	};
	return {
		"x": x * getCos(direction) - y * getSin(direction),
		"y": x * getSin(direction) + y * getCos(direction),
	};
};
const clamp = (x, bound) => Math.max(Math.min(x, bound), -bound);

let showSettings = false;
document.getElementById("settings_icon").onclick = () => {
	const newShowSettings = !showSettings;
	UTILITIES.onClearSearch(false);
	UTILITIES.clearPanes();
	document.getElementById("settings").style.display = newShowSettings ? "" : "none";
	showSettings = newShowSettings;
};

export default UTILITIES;
