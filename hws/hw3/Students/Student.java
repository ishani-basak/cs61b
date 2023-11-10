package Students;

public class Student {

    String name;
    int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public static void motto() {
        System.out.println("Woohoo!");
    }

    public void study() {
        System.out.println("Time to read!");
    }

    public void greet(Student s) {
        System.out.println("Hi fellow student, I'm " + name);
    }

    public static void main(String[] args) {
        Student dominic = new Student("Dominic", 19);
        Student dexter = new BerkeleyStudent("Dexter", 21);
        Student shirley = new StanfordStudent("Shirley", 20);
        BerkeleyStudent grace = new CS61BStudent("Grace", 20);
        CS61BStudent kyle = new CS61BStudent("Kyle", 19);
        StanfordStudent vidya = new StanfordStudent("Vidya", 20);
        BerkeleyStudent claire = new BerkeleyStudent("Claire", 19);

        dominic.study();
        dexter.study();
        shirley.study();
        grace.study();
        kyle.study();
        vidya.study();
        System.out.println();

        dominic.greet(vidya);
        grace.greet(kyle);
        kyle.greet(grace);
        grace.greet(shirley);
        grace.greet(vidya);
        vidya.greet(kyle);
        System.out.println();

        /* ((CS61BStudent) vidya).study(); gives compiler error */
        ((BerkeleyStudent) kyle).study();
        /* ((CS61BStudent) shirley).study(); gives runtime error */
        ((BerkeleyStudent) dexter).greet(kyle);
        vidya.greet((StanfordStudent) shirley);
        System.out.println();

        dominic.motto();
        dexter.motto();
        shirley.motto();
        grace.motto();
        kyle.motto();
        vidya.motto();
        System.out.println();

        System.out.println(BerkeleyStudent.avgGPA);
        System.out.println(CS61BStudent.avgGPA);
        /* System.out.print(dexter.avgGPA); gives compiler error */
        System.out.println(grace.avgGPA);
        System.out.println(kyle.avgGPA);
        System.out.println(claire.avgGPA);
        /* System.out.println(CS61BStudent.favClassAtCal); gives runtime error */
        System.out.println();

        /* System.out.print(CS61BStudent.favClassAtCal); gives compiler error */
        System.out.println(grace.favClassAtCal);
        System.out.println(kyle.favClassAtCal);
        System.out.print(claire.favClassAtCal);
    }
}