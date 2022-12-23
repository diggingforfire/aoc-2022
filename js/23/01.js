const fs = require("fs");
const path = require("path");

const rounds = 10;

const grid = fs
    .readFileSync(path.join(__dirname, "input.txt"), "utf8")
    .split("\r\n")
    .map((l, y) => ('.'.repeat(rounds / 2 + 1) + l + '.'.repeat(rounds / 2 + 1)).split(''));

for (let i = 0; i < rounds / 2 + 1; i++) {
    grid.unshift(Array.from('.'.repeat(grid[0].length)));
    grid.push(Array.from('.'.repeat(grid[0].length)));
}

const elves = grid.flatMap((row, y) => row.map((e, x) => ({e: e, x: x, y: y})).filter(e => e.e === '#'));

const fourDirections = ['N', 'S', 'W', 'E'];
const eightDirections = {
    'N' : [0, -1],
    'NE': [1, -1],
    'NW': [-1, -1],
    'S' : [0, 1],
    'SE': [1, 1],
    'SW': [-1, 1],
    'W' : [-1, 0],
    'E' : [1, 0],
};

const adjacentPositions = [
    eightDirections.N, 
    eightDirections.NE,  
    eightDirections.E,  
    eightDirections.SE,  
    eightDirections.S,  
    eightDirections.SW, 
    eightDirections.W, 
    eightDirections.NW, 
];

const eightDirectionsOrder = ['N', 'NE', 'NW', 'S', 'SE', 'SW', 'W', 'NW', 'SW', 'E', 'NE', 'SE'];

var groupBy = function(xs, key) {
    return xs.reduce(function(rv, x) {
      (rv[x[key]] = rv[x[key]] || []).push(x);
      return rv;
    }, {});
};

for (let i = 0; i < rounds; i++) {
    // first half of the round: proposals

    for (const elf of elves) {
        const adjacents = adjacentPositions.map(p => 
            elf.y + p[1] >= 0 && elf.y + p[1] < grid.length &&
            elf.x + p[0] >= 0 && elf.x + p[0] < grid[0].length &&   
            grid[elf.y + p[1]][elf.x + p[0]] === '#');
        
        if (adjacents.some(a => !!a)) {
            for (const direction of fourDirections) {     
                directionIndex = eightDirectionsOrder.indexOf(direction);
                const directions = eightDirectionsOrder.slice(directionIndex, directionIndex + 3);
                
                if (!directions.some(d =>  
                    elf.y + eightDirections[d][1] >= 0 && elf.y + eightDirections[d][1]  < grid.length &&
                    elf.x + eightDirections[d][0] >= 0 && elf.x + eightDirections[d][0] < grid[0].length &&   
                    grid[elf.y + eightDirections[d][1]][elf.x + eightDirections[d][0]] === '#')) {

                        elf.proposal = {
                            direction: direction,
                            position: {
                                x: elf.x + eightDirections[direction][0],
                                y: elf.y + eightDirections[direction][1]
                            }
                        };

                        elf.proposedDirection = elf.proposal.position.x + '' + elf.proposal.position.y;
                        //propose
                        break;
                }
            }
        }
    }

    // second half of the round: move
    const groups = groupBy(elves, 'proposedDirection');

    for (key of Object.keys(groups).filter(k => groups[k].length === 1)) {
        const elf = groups[key][0];
        // move elf

        grid[elf.y][elf.x] = '.'
        elf.x = elf.proposal.position.x;
        elf.y = elf.proposal.position.y;
        grid[elf.y][elf.x] = '#';

        elf.proposedDirection = null;
    }

    // change order of directions
    fourDirections.push(fourDirections.shift());
}

const minY = Math.min(...(elves.map(e => e.y)));
const maxY = Math.max(...(elves.map(e => e.y)));
const minX = Math.min(...(elves.map(e => e.x)));
const maxX = Math.max(...(elves.map(e => e.x)));

let sum = 0;
for (let y = minY; y <= maxY; y++) {
    for (let x = minX; x <= maxX; x++) {
        if (grid[y][x] == '.') {
            sum++;
        }
    }
}

console.log(sum);

function debug(grid) {
    let output = '';
    for (let y = 0; y < grid.length; y++) {
        for (let x = 0; x < grid[0].length; x++) {
            output += grid[y][x];
        }
        output += '\n';
    }
    console.log(output);
}