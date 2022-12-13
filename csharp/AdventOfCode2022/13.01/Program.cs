IntOrList ParsePackets(string packets)
{
    var stack = new Stack<IntOrList>();
    IntOrList current = null;
    List<char> chars = new List<char>();
    foreach (var c in packets)
    {
        if (c == '[')
        {
            var newList = new IntOrList(null, new List<IntOrList>());
            current?.list.Add(newList);
            current = newList;
            stack.Push(current);
        }
        else if (c == ']')
        {
            if (chars.Count > 0)
            {
                current.list.Add(new IntOrList(int.Parse(new string(chars.ToArray())), new List<IntOrList>()));
                chars.Clear();
            }

            stack.Pop();
            if (stack.Count == 0) break;            
            current = stack.Peek();
        }
        else
        {
            if (c != ',') 
            {
                chars.Add(c);
            }       
            else if (chars.Any())
            {
                current.list.Add(new IntOrList(int.Parse(new string(chars.ToArray())), new List<IntOrList>()));
                chars.Clear();
            }
        }
    }

    return current;
}

bool? CheckOrder(IntOrList one, IntOrList two)
{
    // Compare two integers
    if (one.value != null && two.value != null)
    {      
        if (one.value == two.value) return null;
        return one.value < two.value;
    }
    // Compare two lists
    else if (one.value == null && two.value == null)
    {
        var pairs = one.list.Zip(two.list);
        bool? check = null;
        foreach (var pair in pairs)
        {
            check = CheckOrder(pair.First, pair.Second);
            if (check.HasValue) return check.Value;        
        }
        if (check == null)
        {
            if (one.list.Count < two.list.Count) return true; // Left side ran out of items, so inputs are in the right order            
            else if (one.list.Count > two.list.Count) return false; // Right side ran out of items, so inputs are not in the right order      
        }
    }
    // Compare integer and list
    else if (one.list.Any() && !two.list.Any() || two.list.Any() && !one.list.Any())
    { 
        if (!one.list.Any()) one = new IntOrList(null, new List<IntOrList> { one });
        if (!two.list.Any()) two = new IntOrList(null, new List<IntOrList> { two });
        return CheckOrder(one, two);
    }
    else 
    {
        if (one.value == null && two.value != null) return true; // Left side ran out of items, so inputs are in the right order       
        if (one.value != null && two.value == null) return false; // Right side ran out of items, so inputs are not in the right order  
    }

    return null;
}

int i = 1;
int sum = 0;

var input = (await File.ReadAllTextAsync("input.txt"))
    .Split($"{Environment.NewLine}{Environment.NewLine}")
    .Select(pair => pair.Split(Environment.NewLine));

foreach (var pair in input)
{
    var left = ParsePackets(pair[0]);
    var right = ParsePackets(pair[1]);
    if (CheckOrder(left, right) ?? false) 
    {
        sum += i;
    } 
    i++;
}

Console.WriteLine(sum);

record class IntOrList(int? value, List<IntOrList> list);