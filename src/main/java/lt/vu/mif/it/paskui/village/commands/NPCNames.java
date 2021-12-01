package lt.vu.mif.it.paskui.village.commands;

import java.util.Random;

public enum NPCNames {

    JOE("Joe"),
    OLIVER("Oliver"),
    JAMES("James"),
    LUCAS("Lucas"),
    BOB("Bob"),
    BENJAMIN("Benjamin"),
    TRAVIS("Travis"),
    FRANK("Frank"),
    TYLER("Tyler"),
    TYRONE("Tyrone"),
    JUAN("Juan"),
    JAKE("Jake"),
    ANDRE("Andre"),
    NAZAR("Nazar"),
    WILLIAM("William"),
    ANTONIO("Antonio"),
    HENRY("Henry"),
    ETHAN("Ethan"),
    MATEO("Mateo"),
    DAVID("David"),
    CARLOS("Carlos"),
    ROBERT("Robert"),
    GEORGE("George"),
    LOUIS("Luis"),
    JESUS("Jesus"),
    JACK("Jack"),
    DANIEL("Daniel"),
    HARRY("Harry"),
    ADAM("Adam"),
    SEAN("Sean"),
    KYLE("Kyle"),
    PATRICK("Patrick"),
    MUHAMMED("Muhammed"),
    MILES("Miles"),
    KEVIN("Kevin"),
    PAUL("Paul"),
    PETER("Peter"),
    CONNOR("Connor"),
    TREVOR("Trevor"),
    EMIL("Emil"),
        ;

    private final String name;


    NPCNames(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static NPCNames getRandomName() {
        int id = new Random().nextInt(NPCNames.values().length);
        return NPCNames.values()[id];
    }


}
