const fs = require("fs");
const path = require("path");

const grid = fs
    .readFileSync(path.join(__dirname, "input.txt"), "utf8")
    .split("\r\n")
    .map((l, y) => l.split('').map((n, x) => ({n:parseInt(n), x, y}) ));

const trees = grid.flatMap(g => g);

const innerTrees = trees.filter(t => t.x > 0 && t.x < grid[0].length -1 && t.y > 0 && t.y < grid.length -1);

const visible = innerTrees.filter(it => 
    !trees.some(t => t.x < it.x && t.y == it.y && t.n >= it.n) || // left
    !trees.some(t => t.x > it.x && t.y == it.y && t.n >= it.n) || // right
    !trees.some(t => t.x == it.x && t.y > it.y && t.n >= it.n) || // up
    !trees.some(t => t.x == it.x && t.y < it.y && t.n >= it.n)); // down

const outer = (grid.length * 2) + ((grid[0].length -2) * 2);

console.log(visible.length + outer);