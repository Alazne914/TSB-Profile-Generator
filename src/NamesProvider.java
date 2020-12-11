public class NamesProvider {

    private static String[] firstNames = {"Pieter","Jan","Henk","Lucas","Sem","Cas","Luc","Luuk","Sam","Jorg","Gijs","Siem","Senna","Thijmen","Tijmen","Roos","Amber","Leone","Loes","Lente","Jordy","Rick","Kees","Cees","Mark","Marc","Mike"};
    private static String[] lastNames = {"Prakken","Vos","de Jong","Jong","de Vries","Loman","ten Berge","ter Aa","Hannink","Wiggers","Oonk","Scholten","ter Geest","ter Beest","van der Geest","Marksen","Tiehuis","Hooghuis","Horck"};

    public static String getFirstName()
    {
        int i = (int) (Math.random()*firstNames.length);
        return firstNames[i];
    }

    public static String getLastName()
    {
        int i = (int) (Math.random()*lastNames.length);
        return lastNames[i];
    }

}
