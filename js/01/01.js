const fs = require("fs");
const path = require("path");

const groups = fs
    .readFileSync(path.join(__dirname, "input.txt"), "utf8")
    .split("\n\n")
    .map(line => line.split("\n").map(x => parseInt(x)).reduce((a, b) => a + b));
    
console.log(Math.max(...groups));