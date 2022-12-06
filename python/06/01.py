input = open("input.txt", "r", encoding="utf-8-sig").read()
windows = (input[i:i+4] for i in range(len(input) - 3))
filtered = filter(lambda c: len(set(c)) == 4, windows)
print(input.index(next(filtered, None)) + 4)