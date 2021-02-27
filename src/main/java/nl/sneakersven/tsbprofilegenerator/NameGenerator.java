package nl.sneakersven.tsbprofilegenerator;

public class NameGenerator {

    private static String[] firstNames = { "Abel","Arnold","Ali","Bart","Barry","Bas","Ben","Beau","Bob","Bobby","Bram","Cas",
            "Carl","Chiel","Chris","Daan","Daniel","Danny","Dirk","Ed","Emiel","Frits","Filip","Floris","Florian","Frank",
            "Giel","Guido","Gerard","Hein","Henk","Hans","Hidde","Ian","Ivan","Jaap","Jens","Justin","Kay","Kas","Klaas",
            "Kwinten","Lars","Luuk","Luc","Lennard","Leon","Maarten","Mark","Michiel","Micheal","Mathijs","Nick","Nico",
            "Olivier","Okke","Pablo","Pepijn","Pieter","Peter","Quinten","Quirijn","Rens","Rafael","Renzo","Rick","Rik",
            "Sven","Sander","Sam","Simon","Sem","Stan","Thyco","Thijs","Teo","Theo","Thijmen","Ties","Uri","Victor",
            "Vince","Vincent","Walter","Willem","Wessel","Wilfred","Wim","Xander","Xavier","Yannick","Yvan","Yvo",
            "Zander","Aafje","Aafke","Anne","Amber","Anneloes","Anouk","Babet","Bente","Bo","Britt","Claire","Cindy",
            "Carmen","Celine","Dagmar","Denise","Daisy","Daphne","Demi","Dianne","Dionne","Edith","Eefje","Eline","Els",
            "Frank","Fleur","Floor","Frederique","Geertje","Gwen","Guusje","Hanna","Helen","Hilda","Ilse","Ilona","Inge",
            "Isa","Isabel","Jade","Jorieke","Janne","Jansje","Jessica","Joke","Johanna","Karen","Karinne","Klaasje","Kyra",
            "Leone","Lynn","Linda","Loes","Loise","Maartje","Maaike","Malou","Marit","Mila","Nicky","Nina","Naomi",
            "Noa","Noor","Nienke","Olivia","Paula","Pien","Puck","Quirine","Rachel","Renske","Renate","Romy","Roos","Rosa",
            "Sabine","Sanne","Selina","Sanna","Suzanne","Tara","Tanja","Tessa","Tess","Ursula","Valerie","Vanessa","Vera",
            "Wieke","Willeke","Willemijn","Xena","Yasmin","Yara","Yolanda","Yvette","Yvonne","Zara","Zoë" };

    private static String[] lastNames = { "de Jong","Jansen","Janssen","de Vries","van der Berg","van den Berg","van de Berg",
            "van Dijk","Bakker","Visser","Smit","Meijer","Meyer","de Boer","Mulder","de Groot","Bos","Vos","Peters",
            "Hendriks","van Leeuwen","Dekker","Brouwer","de Wit","Dijkstra","Smits","de Graaf","van der Meer","van de Meer",
            "Kok","Jacobs","de Haan","Vermeulen","van der Veen","van de Veen","van den Broek","de Bruijn","de Bruyn",
            "de Bruin","Schouten","Scholten","van Beek","Willems","Prins","Kuiper","Kuijper","Huisman","Timmer","de Vos",
            "de Lange","Bosch","Driessen","Loman","Sanders","Hemel","Mol","de Leeuw","Schipper","de Koning","Willemsen" };

    public static String getFirstName() {
        return firstNames[ (int) (Math.random()*firstNames.length) ];
    }

    public static String getLastName() {
        return lastNames[ (int) (Math.random()*lastNames.length) ];
    }

}
