const fs = require("fs");
const path = require("path");

var groupBy = function(xs, key) {
    return xs.reduce(function(rv, x) {
      (rv[x[key]] = rv[x[key]] || []).push(x);
      return rv;
    }, {});
};

const cubes = fs
    .readFileSync(path.join(__dirname, "input.txt"), "utf8")
    .split("\r\n")
    .map(line => line.split(",").map(c => parseInt(c)));

const minX = Math.min(...cubes.map(c => c[0]));
const maxX = Math.max(...cubes.map(c => c[0]));

const minY = Math.min(...cubes.map(c => c[1]));
const maxY = Math.max(...cubes.map(c => c[1]));

const minZ = Math.min(...cubes.map(c => c[2]));
const maxZ = Math.max(...cubes.map(c => c[2]));


const cache = {};

const getNeighbours = function(node, nodes, cubes) {
    const key = node.x + "-" + node.y + "-" + node.z;

    if (cache.hasOwnProperty(key)) {
        return cache[key];
    }

    const neighbours = nodes.filter(n => !cubes.some(c => 
        c.x === n.x && c.y === n.y && c.z === n.z) &&
        n.x === node.x - 1 && n.y === node.y && n.z === node.z || 
        n.x === node.x + 1 && n.y === node.y && n.z === node.z || 
        n.x === node.x && n.y === node.y - 1 && n.z === node.z || 
        n.x === node.x && n.y === node.y + 1 && n.z === node.z || 
        n.x === node.x && n.y === node.y && n.z === node.z - 1 || 
        n.x === node.x && n.y === node.y && n.z === node.z + 1
    );

    if (!cache.hasOwnProperty(key)) {
        cache[key] = neighbours;
    }

    return neighbours;
}

const bfs = function(start, target, nodes) {
    for (const node of nodes) { node.visited = false; }
    for (key in cache) {
        for (n of cache[key]) {
            n.visited = false;
        }
    }

    start.visited = true;
    const queue = [];
    queue.push(start);

    while (queue.length > 0) {
        const current = queue.shift();
        if (current.x === target.x && current.y === target.y && current.z === target.z) {
            return true;
        }

        for (neighbour of getNeighbours(current, nodes, cubes)) {
            if (!neighbour.visited) {
                neighbour.visited = true;
                queue.push(neighbour);
            }
        }
    }

    return false;
}

function getConnections(cube, cubes) {
    return cubes.map(cube2 => {
        const connection = {
            cube,
            cube2,
            connectionType: null
        }
        
        const xdiff = Math.abs(cube[0] - cube2[0]);
        const ydiff =  Math.abs(cube[1] - cube2[1]);
        const zdiff = Math.abs(cube[2] - cube2[2]);

        if (xdiff === 0 && ydiff === 0 && zdiff === 1) {
            connection.connectionType = 'xy';
            connection.connectionType += (cube2[2] - cube[2]);
        } else if (xdiff === 0 && zdiff === 0 && ydiff === 1) {
            connection.connectionType = 'xz';  
            connection.connectionType += (cube2[1] - cube[1]);
        } else if (ydiff === 0 && zdiff === 0 && xdiff === 1) {
            connection.connectionType = 'yz'; 
            connection.connectionType += (cube2[0] - cube[0]);
        }
        return connection;
    });
}

const cubesWithConnections = cubes.map(c => getConnections(c, cubes.filter(cc => cc != c)));

const connectedSides = cubesWithConnections.map(cube => {
    const connections = cube.filter(connection => !!connection.connectionType);
    return Object.keys(groupBy(connections, 'connectionType')).length;
} );    

// search space
const allNodes = []

for (var x = minX - 1; x <= maxX + 1; x++) {
    for (var y = minY - 1; y <= maxY + 1; y++) {
        for (var z = minZ - 1; z <= maxZ + 1; z++) {
            if (!cubes.some(c => c[0] === x && c[1] === y && c[2] === z)) {
                allNodes.push({x, y, z});
            }
        }
    }
}

const target = {x: minX - 2 , y: minY, z: minZ}; // reachable 'corner' of the field
allNodes.push(target);
const airpockets = []

let i = 1;
for (const node of allNodes) {
    console.log(i++ + "/" + allNodes.length);
    if (! (node.x === target.x && node.y === target.y && node.z === target.z)) {
        // if the reachable target cannot be reached by the node, it must be an airpocket
        if (!bfs(node, target , allNodes)) {
            airpockets.push(node);
        }
    }
}

const airpocketConnections = airpockets.map(a => getConnections([a.x, a.y, a.z], cubes));
const connectedAirpocketSides = airpocketConnections.map(cube => {
    const connections = cube.filter(connection => !!connection.connectionType);
    return Object.keys(groupBy(connections, 'connectionType')).length;
} );    

const totalAirpocketSides = connectedAirpocketSides.reduce((a, b) => a + b);


const totalConnectedSides = connectedSides.reduce((a, b) => a + b);

console.log((cubes.length * 6 - totalConnectedSides) - totalAirpocketSides);