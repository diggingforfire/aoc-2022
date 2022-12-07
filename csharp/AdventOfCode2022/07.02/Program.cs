using _07._02;

var lines = (await File.ReadAllLinesAsync("input.txt")).Skip(1).ToArray();
var root = new DirOrFile(null, "/");
var commands = lines.Select((line, i) => new { Line = line, Index = i + 1 }).Where(line => line.Line[0] == '$');

DirOrFile current = root;

foreach (var command in commands)
{
    if (command.Line == "$ ls")
    {
        var list = lines.Skip(command.Index).TakeWhile(l => !l.StartsWith("$"));
        current.Items.AddRange(list.Select(name => new DirOrFile(current, name)));
    }
    else if (command.Line.StartsWith("$ cd"))
    {
        var target = command.Line.Split("$ cd", StringSplitOptions.RemoveEmptyEntries)[0].Trim();
        if (target == "..")
        {
            current = current.Parent;
        }
        else
        {
            current = current.Items.Single(i => i.Name.Split(" ", StringSplitOptions.RemoveEmptyEntries)[1] == target);
        }

    }
}


List<DirOrFile> dirs = new();

void Traverse(DirOrFile current, List<DirOrFile> total)
{
    total.Add(current);
    foreach (var item in current.Items.Where(i => !i.IsFile))
    {
        Traverse(item, total);
    }
}

Traverse(root, dirs);

int availableSpace = 70000000 - root.Size;
int amountToDelete = 30000000 - availableSpace;

var size = dirs.Where(dir => dir.Size >= amountToDelete).Min(dir => dir.Size);
Console.WriteLine(size);