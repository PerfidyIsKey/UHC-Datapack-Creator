import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class QuickTest {
    public static void main(String[] args) {
        ArrayList<String> quotes = new ArrayList<>();

        quotes.add("<Erik> Ik ben nooit brak, ik ben alleen soms boerensmoe.");
        quotes.add("<Jacco> Zie je deze bril? Ik ben gemaakt voor perceptie.");
        quotes.add("<Raymond> Ik versta je niet, ik bel Dion wel.");
        quotes.add("<Kees> Behangen is een soort lamineren, maar dan op muren");
        quotes.add("<Rob> Ja ik ben geen aero expert maar...\\n<Danick> Ja maar Rob, welke expert ben jij wel dan?");
        quotes.add("<Data> *exists*\\n<Stan> This looks good");
        quotes.add("<Person1> Kees wat ben je aan het doen?\\n<Kees> Kabels fixen\\n<Person1> Maar hoe dan?\\n<Kees> Ja dat weet ik eigenlijk ook niet.");
        quotes.add("<Max> Wat is scrutineering?");
        quotes.add("<Raymond> Gisteren was er toch ook een race?\\n<Person2> Nee, toen was kwalificatie\\n<Raymond> Ja maar toen deed iedereen toch ook zn best?");
        quotes.add("<Marco> Waar staat de BOM?");
        quotes.add("<Lars> Ik had genoeg dingen die in NX niet pasten, en dan in het echt ook niet, das best logisch");
        quotes.add("<Stan> Moeten we onze eigen tent meenemen dan?");
        quotes.add("<Jorick> Ik bereken net een area en het is precies 69.420\\n<Joos> Dan moet het wel goed zijn");
        quotes.add("<Dion> Raymond, leg deze foto's in chronologische volgorde\\n<Raymond> Wat voor chronologische volgorde? In de tijd?");
        quotes.add("<Joos> Mijn persoonlijkheid is op dit moment bestuur.");
        quotes.add("<Stan> Ik neem wel een camping stoeltje mee.");
        quotes.add("<Marco> We bouwen toch auto's, geen stad!");
        quotes.add("<Person1> Ik was laatst de abc-formule vergeten\\n<Raymond> Dat is toch a^2+b^2=c^2?");
        quotes.add("<Danick> We moeten hier ook tosti's... uh softies kunnen zijn!");
        quotes.add("<Stan> Weet ik veel hoe je navigeert in een tent.");
        quotes.add("<Jorick> Eigenlijk is het gewoon mooi dat we leven");
        quotes.add("<Raymond> Mocht je toevallig nog bandenhoezen vinden in de supermarkt, dan mag je die ook meenemen.");
        quotes.add("<Jacco> Je hebt gewoon salmonella of een goeie dag");
        quotes.add("<Stan> Ik ben niet zo lui als jullie denken, maar het scheelt niet veel.");
        quotes.add("<Jorick> Echte mannen rijden op de velg.");
        quotes.add("<Stan> Hier in Spanje zijn ook een hoop Spanjaarden.");
        quotes.add("<Luc> Ik heb altijd een hele goedkope carnival.");
        quotes.add("<Teun> Zullen we proberen op het RES kanaal van het andere team te zitten en dan een AS Emergency triggeren?\\n<Dion> Dat mag niet.");
        quotes.add("<Nicolas> What happened after the lunch?");
        quotes.add("<UPC ecoRacing> Our goal was to finish Trackdrive, not win it.");
        quotes.add("<Danick> Chris wat staat er op je voorhoofd?\\n<Chris> De handtekening van Teun voor de escalatie mix!");
        quotes.add("<Jorick> Luchtvissen");
        quotes.add("<Jilez> Kijk daar een Ice!");
        quotes.add("<Jorick> Je bent pas geboren als je 6 bent.");
        quotes.add("<Chris' small mesh FEM> *being slow*\\n<Rody> I think you meshed up");
        quotes.add("<Noah> Zijn Nederlandse boetes wel voor URE?");
        quotes.add("<Joos> Ik heb ook een leven naast mijn alcoholisme.");
        quotes.add("<Yannick> URE zorgt er niet voor dat je minder tijd hebt, je dagen worden gewoon langer\\n<Ricardo> En de dag erna korter");
        quotes.add("<Jacco> Ik krijg van Erik geen appels, wel SLAM updates");
        quotes.add("<Jacco> Als Kees een driverless auto is druk ik nou op de noodstop");
        quotes.add("<Bas> Ultra Hard Core Minecraft betekent net zoveel voor mij als de URE13 voor Wijshoff");

        try {
            File file = new File("Files\\URE\\quotes.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String quote: quotes) {
                writer.write(quote);
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {

        }

    }
}
