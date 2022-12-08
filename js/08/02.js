const fs = require("fs");
const path = require("path");

const grid = fs
    .readFileSync(path.join(__dirname, "input.txt"), "utf8")
    .split("\r\n")
    .map((l, y) => l.split('').map((n, x) => ({n:parseInt(n), x, y}) ));

const trees = grid.flatMap(g => g);

const inclusiveTakeWhile = (fn, arr) => {
    const [x, ...xs] = arr;
  
    if (arr.length > 0 && fn(x)) {
      return [x, ...inclusiveTakeWhile(fn, xs)]
    } else {
      if (x) {
        return [x];
      } else {
        return [];
      }
  }
};
  
const filterTrees = (fn, tree, trees, reverse) => {
  return inclusiveTakeWhile(t => t.n < tree.n, reverse ? trees.filter(fn).reverse() : trees.filter(fn));
};

const visible = trees.map(tree => 
    (
        {
            left: filterTrees(t => t.x < tree.x && t.y == tree.y, tree, trees, true),
            right: filterTrees(t => t.x > tree.x && t.y == tree.y, tree, trees),
            up: filterTrees(t => t.x == tree.x && t.y < tree.y, tree, trees, true),
            down: filterTrees(t => t.x == tree.x && t.y > tree.y, tree, trees),
        }
    )
);

const scores = visible.map(t => t.left.length * t.right.length * t.up.length * t.down.length);

console.log(Math.max(...scores));