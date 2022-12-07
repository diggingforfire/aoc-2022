using _07._01;

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

int SumRecursive(DirOrFile dir)
{
    return dir.Items.Where(i => !i.IsFile).Sum(i => SumRecursive(i) + (i.Size <= 100000 ? i.Size : 0));
}

Console.WriteLine(SumRecursive(root));