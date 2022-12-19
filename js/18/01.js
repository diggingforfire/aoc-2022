const fs = require("fs");
const path = require("path");

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

const cubesWithConnections = cubes.map(c => getConnections(c, cubes.filter(cc => cc != c)));

const connectedSides = cubesWithConnections.map(cube => {
    const connections = cube.filter(connection => !!connection.connectionType);
    return Object.keys(groupBy(connections, 'connectionType')).length;
} );    

const totalConnectedSides = connectedSides.reduce((a, b) => a + b);

console.log(cubes.length * 6 - totalConnectedSides);