var count =
        (await File.ReadAllLinesAsync("input.txt"))
        .Select(line =>
            line.Split(",", StringSplitOptions.RemoveEmptyEntries)
                .Select(pair => pair.Split("-", StringSplitOptions.RemoveEmptyEntries))
                .Select(pair => new { Min = int.Parse(pair[0]), Max = int.Parse(pair[1]) }).ToArray())
        .Count(pair =>
            (pair[0].Min >= pair[1].Min && pair[0].Max <= pair[1].Max) ||
            (pair[1].Min >= pair[0].Min && pair[1].Max <= pair[0].Max)
        );

Console.WriteLine(count);