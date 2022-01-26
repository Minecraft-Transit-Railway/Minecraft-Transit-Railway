import SETTINGS from "./index.js";

let adjTable = {};

function buildGraph(data) {
    const {blobs, positions, stations, routes, types} = data;
    for (const si1 in stations) {
        let keyName = si1;
        adjTable[keyName] = {};
        for (const routeIndex in routes) {
            const route = routes[routeIndex];
            for (const si2 in route["stations"]) {
                if (route["stations"][si2].split("_")[0] === si1) {
                    /* if (si2 > 0) {
                        let stationId = route["stations"][parseInt(si2)-1].split("_")[0];
                        adjTable[keyName][stationId] = { "route": routeIndex };
                    } */
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
}

function findRoute(data, beginId, endId) {
    // BFS
    let vis = {};
    let fa = {};
    let queue = [ beginId ];
    while (queue.length > 0) {
        for (const edgeTo in adjTable[queue[0]]) {
            const edge = adjTable[queue[0]][edgeTo];
            if (edgeTo === endId) {
                fa[edgeTo] = { "from": queue[0], "route": edge["route"] };
                queue = [];
                break;
            }
            if (!vis[edgeTo]) {
                fa[edgeTo] = { "from": queue[0], "route": edge["route"] };
                queue.push(edgeTo);
                vis[edgeTo] = true;
            }
        }
        queue.shift();
    }
    if (fa[endId]) {
        let result = [];
        let look = endId;
        while (look != beginId) {
            result.unshift({ "to": look, "route": fa[look]["route"] });
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

function findShowPlan(data, planner_begin, planner_end) {
    let html;
    let plan = findRoute(data, planner_begin, planner_end);
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
                if (lastStep["route"] === 0 || data["routes"][lastStep["route"]]["name"] !== data["routes"][step["route"]]["name"]
                    || data["routes"][lastStep["route"]]["color"] !== data["routes"][step["route"]]["color"]) {
                    if (lastStep["route"] === 0) {
                        html += "<div><span class='material-icons'>home</span>" + smallerEnglish(data["stations"][lastStep["to"]]["name"]) + "</div>";
                    } else {
                        html += "<div><span class='material-icons'>transfer_within_a_station</span>" + smallerEnglish(data["stations"][lastStep["to"]]["name"]) + "</div>";
                    }
                    html += "<div style='background-color: " + convertColor(data["routes"][step["route"]]["color"]) + "; " 
                        + "color: " + hex_inverse_bw(convertColor(data["routes"][step["route"]]["color"])) + "; font-weight: bold'>" 
                        + "<span class='material-icons' style='color:inherit'>tram</span>"
                        + smallerEnglish(data["routes"][step["route"]]["name"]) + "</div>";
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
