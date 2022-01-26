import SETTINGS from "./index.js";

let adjTable = {};
let stationPositions = {};

function buildGraph(data) {
    for (const si1 in data["stations"]) {
        let keyName = si1;
        adjTable[keyName] = {};
        for (const routeIndex in data["routes"]) {
            const route = data["routes"][routeIndex];
            for (const si2 in route["stations"]) {
                if (route["stations"][si2].split("_")[0] === si1) {
                    if (si2 > 0) {
                        let stationId = route["stations"][parseInt(si2)-1].split("_")[0];
                        adjTable[keyName][stationId] = { "route": routeIndex };
                    }
                    if (si2 < route["stations"].length - 1) {
                        let stationId = route["stations"][parseInt(si2)+1].split("_")[0];
                        adjTable[keyName][stationId] = { "route": routeIndex };
                    }
                    // Should not break, a route might contain the same station multiple times
                    // break;
                }
            }
        }
    }
    for (const positionKey in data["positions"]) {
        const position = data["positions"][positionKey];
        const x = position["x"];
        const y = position["y"];

        const stationId = positionKey.split("_")[0];
        const color = parseInt(positionKey.split("_")[1]);
        const blob = stationPositions[stationId];
        if (typeof blob === "undefined") {
            stationPositions[stationId] = {
                xMin: x,
                yMin: y,
                xMax: x,
                yMax: y,
            };
        } else {
            blob["xMin"] = Math.min(blob["xMin"], x);
            blob["yMin"] = Math.min(blob["yMin"], y);
            blob["xMax"] = Math.max(blob["xMax"], x);
            blob["yMax"] = Math.max(blob["yMax"], y);
        }
    }
}

// Priority queue (https://stackoverflow.com/a/42919752) (CC BY-SA 3.0)
const top = 0;
const parent = i => ((i + 1) >>> 1) - 1;
const left = i => (i << 1) + 1;
const right = i => (i + 1) << 1;

class PriorityQueue {
  constructor(comparator = (a, b) => a > b) {
    this._heap = [];
    this._comparator = comparator;
  }
  size() {
    return this._heap.length;
  }
  isEmpty() {
    return this.size() == 0;
  }
  peek() {
    return this._heap[top];
  }
  push(...values) {
    values.forEach(value => {
      this._heap.push(value);
      this._siftUp();
    });
    return this.size();
  }
  pop() {
    const poppedValue = this.peek();
    const bottom = this.size() - 1;
    if (bottom > top) {
      this._swap(top, bottom);
    }
    this._heap.pop();
    this._siftDown();
    return poppedValue;
  }
  replace(value) {
    const replacedValue = this.peek();
    this._heap[top] = value;
    this._siftDown();
    return replacedValue;
  }
  _greater(i, j) {
    return this._comparator(this._heap[i], this._heap[j]);
  }
  _swap(i, j) {
    [this._heap[i], this._heap[j]] = [this._heap[j], this._heap[i]];
  }
  _siftUp() {
    let node = this.size() - 1;
    while (node > top && this._greater(node, parent(node))) {
      this._swap(node, parent(node));
      node = parent(node);
    }
  }
  _siftDown() {
    let node = top;
    while (
      (left(node) < this.size() && this._greater(left(node), node)) ||
      (right(node) < this.size() && this._greater(right(node), node))
    ) {
      let maxChild = (right(node) < this.size() && this._greater(right(node), left(node))) ? right(node) : left(node);
      this._swap(node, maxChild);
      node = maxChild;
    }
  }
}

function stationPosition(data, sId) {
    const blob = stationPositions[sId];
    const {xMin, yMin, xMax, yMax} = blob;
    return [ (xMin + xMax) / 2, (yMin + yMax) / 2 ];
}

const SPEED_WALK_APPROX = 4;
const SPEED_TRAIN_APPROX = 20;

function findRoute(data, beginId, endId) {
    let vis = {}, dis = {}, fa = {}, q = new PriorityQueue((a, b) => a[0] > b[0]);
    for (const sId in data["stations"]) {
        vis[sId] = false;
        dis[sId] = Number.MAX_SAFE_INTEGER;
    }
    dis[beginId] = 0;

    // Dijkstra without priority queue optimization
    for (const sId1 in data["stations"]) {
        let u = 0, mind = Number.MAX_SAFE_INTEGER;
        for (const sId2 in data["stations"])
            if (!vis[sId2] && dis[sId2] < mind) u = sId2, mind = dis[sId2];

        vis[u] = true;
        const posU = stationPosition(data, u);
        for (const eEnd in data["stations"]) {
            const posE = stationPosition(data, eEnd);
            const distM = Math.abs(posE[0] - posU[0]) + Math.abs(posE[1] - posU[1]);
            const edge = adjTable[u][eEnd];
            const timeM = distM / (!!edge ? SPEED_TRAIN_APPROX : SPEED_WALK_APPROX);
            if (dis[eEnd] > dis[u] + timeM) {
                dis[eEnd] = dis[u] + timeM;
                if (edge) {
                    fa[eEnd] = { "from": u, "route": edge["route"], "dist": Math.round(distM) };
                } else {
                    fa[eEnd] = { "from": u, "route": -1, "dist": Math.round(distM) };
                }
            }
        }
    }

    for (const sId in dis) {
        console.log(data["stations"][sId]["name"], dis[sId]);
    }

    if (fa[endId]) {
        let result = [];
        let look = endId;
        while (look != beginId) {
            result.unshift({ "to": look, "route": fa[look]["route"], "dist": fa[look]["dist"] });
            look = fa[look]["from"];
        }
        return result;
    } else {
        return null;
    }
}

const convertColor = color => "#" + Number(color).toString(16).padStart(6, "0");
function hex_to_rgb(hex) {
        let result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
        return result ? { 
                r: parseInt(result[1], 16),
                g: parseInt(result[2], 16),
                b: parseInt(result[3], 16) 
        } : null;
}
function hex_inverse_bw(hex) {
        let rgb = hex_to_rgb(hex);
        let luminance = (0.2126*rgb["r"] + 0.7152*rgb["g"] + 0.0722*rgb["b"]);
        return (luminance < 140) ? "#ffffff": "#000000";
}

function smallerEnglish(name) {
    const nameSplit = name.split("|");
    let html = "";
    for (const nameSplitIndex in nameSplit) {
        const namePart = nameSplit[nameSplitIndex];
        if (SETTINGS.isCJK(namePart)) {
            html += namePart + " ";
        } else {
            html += "<small>" + namePart + "</small> ";
        }
    }
    return html;
}

function getRouteInternal(data, route) {
    if (route === -1) return { "name": "#WALK", "color": 0 };
    return data["routes"][route];
}

function findShowPlan(data, planner_begin, planner_end) {
    let html, plan;
    if (data === null || planner_begin === 0 || planner_end === 0) {
        plan = null;
    } else {
        plan = findRoute(data, planner_begin, planner_end);
        console.log(plan);
    }
    if (plan == null) {
        if (planner_begin === 0 || planner_end === 0) {
            // Hint clicking the two buttons
            html = "<span class='material-icons'>info</span>: <span class='material-icons'>mouse</span> <span class='material-icons'>arrow_right_alt</span>" +
                "\"<span class='material-icons'>home</span>, <span class='material-icons'>flag</span>\"";
        } else {
            // No route
            html = "<span class='material-icons'>warning</span>: <span class='material-icons'>route</span> <span class='material-icons'>clear</span>"
        }
    } else if (plan.length == 0) {
        // Recommend walking
        html = "<span class='material-icons'>info</span>: <span class='material-icons'>directions_walk</span>"
    } else {
        let lastStep = { "to": planner_begin, "route": 0 };
        html = "";
        for (const stepId in plan) {
            const step = plan[stepId];
            if (step["route"] != lastStep["route"]) {
                // Do not show the transfer between rapid trains and local trains of the same line
                if (lastStep["route"] === 0 || getRouteInternal(data, lastStep["route"]["name"]) !== getRouteInternal(data, step["route"])["name"]
                    || getRouteInternal(data, lastStep["route"])["color"] !== getRouteInternal(data, step["route"])["color"]) {
                    if (lastStep["route"] === 0) {
                        html += "<div><span class='material-icons'>home</span>" + smallerEnglish(data["stations"][lastStep["to"]]["name"]) + "</div>";
                    } else {
                        html += "<div><span class='material-icons'>transfer_within_a_station</span>" + smallerEnglish(data["stations"][lastStep["to"]]["name"]) + "</div>";
                    }
                    if (step["route"] === -1) {
                        html += "<div style='background-color: #ddd'><span class='material-icons'>directions_walk</span> " + step["dist"] + "m</div>";
                    } else {
                        html += "<div style='background-color: " + convertColor(data["routes"][step["route"]]["color"]) + "; " 
                            + "color: " + hex_inverse_bw(convertColor(data["routes"][step["route"]]["color"])) + "; font-weight: bold'>" 
                            + "<span class='material-icons' style='color:inherit'>tram</span>"
                            + smallerEnglish(data["routes"][step["route"]]["name"]) + "</div>";
                    }
                }
            }
            if (step["to"] === planner_end) {
                html += "<div><span class='material-icons'>flag</span>" + smallerEnglish(data["stations"][step["to"]]["name"]) + "</div>";
            }
            lastStep = step;
        }
    }
    document.getElementById("trip_planner_path").innerHTML = html;
    document.getElementById("planner_begin_text").innerHTML = (planner_begin === 0) ? "..." : smallerEnglish(data["stations"][planner_begin]["name"]);
    document.getElementById("planner_end_text").innerHTML = (planner_end === 0) ? "..." : smallerEnglish(data["stations"][planner_end]["name"]);
}

export { buildGraph, findShowPlan }
