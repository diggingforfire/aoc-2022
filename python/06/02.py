input = open("input.txt", "r", encoding="utf-8-sig").read()
windows = (input[i:i+14] for i in range(len(input) - 13))
filtered = filter(lambda c: len(set(c)) == 14, windows)
print(input.index(next(filtered, None)) + 14)