int cycle = 0;
var instructions = (await File.ReadAllLinesAsync("input.txt"))
    .Select(line => line.Split(" ", StringSplitOptions.RemoveEmptyEntries))
    .Select(i => new
    {
        Instruction = i[0],
        Value = i.Length > 1 ? int.Parse(i[1]) : 0,
        Cycle = i[0] == "noop" ? (cycle += 1) : i[0] == "addx" ? (cycle += 2) : 0
    })
    .ToArray();

int x = 1;

for (int i = 1; i < cycle; i++)
{
    if ((i%40) - 1 >= x - 1 && (i%40) - 1 <= x + 1) Console.Write("#");
    else Console.Write(".");
    
    if (i % 40 == 0) Console.WriteLine("");
    var instruction = instructions.SingleOrDefault(instruction => instruction.Cycle == i && instruction.Instruction == "addx");
    x += instruction?.Value ?? 0;
}