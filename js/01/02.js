const fs = require("fs");
const path = require("path");

const sum = fs
    .readFileSync(path.join(__dirname, "input.txt"), "utf8")
    .split("\n\n")
    .map(line => line.split("\n").map(x => parseInt(x)).reduce((a, b) => a + b))
    .sort()
    .slice(-3)
    .reduce((a, b) => a + b);

console.log(sum);