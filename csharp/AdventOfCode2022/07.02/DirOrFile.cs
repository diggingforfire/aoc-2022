namespace _07._02
{
    internal class DirOrFile
    {
        public DirOrFile(DirOrFile parent, string name)
        {
            Parent = parent;
            Name = name;
        }

        public DirOrFile Parent { get; set; }
        public string Name { get; set; }
        public bool IsFile => Items.Count == 0;
        public int ItemCount => (IsFile ? 1 : 0) + Items.Sum(i => i.ItemCount);
        public bool AtMost(int i) => Size <= i;

        public int Size => IsFile ? int.Parse(Name.Split(" ", StringSplitOptions.RemoveEmptyEntries)[0]) : Items.Sum(i => i.Size);

        public List<DirOrFile> Items { get; set; } = new List<DirOrFile>();
    }
}
