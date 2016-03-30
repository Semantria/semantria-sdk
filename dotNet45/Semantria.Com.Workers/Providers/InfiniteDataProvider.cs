using System;
using System.IO;
using System.Dynamic;
using System.Collections.Generic;

using Semantria.Com.CSV;

namespace Semantria.Com.Workers
{
    public class InfiniteDataProvider : IDataProvider
    {
        #region Private Members

        static LinkedList<string> _linkedList = new LinkedList<string>(new List<string>()
        {
            "Herself compares Repub. candidates to terrorists. I compare her to Amsterdam whores, who screw anyone for money and power. @HillaryClinton",
            "@Asher_P_Fly @BreitbartNews @HillaryClinton Clinton fatigue? Sure we're tired of Bill's lying perfidy and Hillary's blatant criminality.",
            "@MarcABelisle @ReverbPress wow great article and just continues to prove that @HillaryClinton is presidential and well Bush is a Bush",
            "Very excited @HillaryClinton DNC supporters gathered in Minneapolis 4 update &amp; 2 hear from HRC, who committed 2 building state parties.",
            "Retweeted Hillary Clinton (@HillaryClinton):  Heartbroken and angry. We must act to stop gun violence, and we... http://t.co/VXzZbSGi7M",
            "@GlobeOpinion  This is about police corruption &amp; abuse of power. Nothing to do with @BernieSanders. You should be ashamed @fstockman",
            "@HillaryClinton Why are the women working on your campaign paid less than the men?",
            "@nytpolitics @maggieNYT It's a fact that @HillaryClinton is building her campaign but her motive in sending memos is your opinion. #FITN",
            "@HillaryClinton can be proud that her server was more secure server than the government's -- no hacks or security breaches with her emails!",
            "@BernieSanders I don't deny it exists. I deny that it's our greatest threat above real threats like ISIS.",
            "@Morning_Joe starting to rant about @HillaryClinton . Time to watch @CNN @ChrisCuomo",
            "@USATODAY @HillaryClinton Yawn...we still know you LIED about the EMAILS",
            "Remember that time @HillaryClinton was dead broke?",
            "UL: @MartinOMalley said @HillaryClinton is jeopardizing their party's chance of victory in 2016. http://t.co/1CqR6WiOCQ #nhpolitics #fitn",
            "@AmericaNewsroom #MarthaMacCallum Huckster:FORCE raped 10 yo to have baby! Rubio:Let Women die! @GOP:YAWN @HillaryClinton: I'll FIGHT 4 U!!!",
            "@realDonaldTrump @jp_sitles @hillaryclinton supported and funded terrorists! She's a total wackjob !",
            "Honored to be the DNCViceChair chosen to introduce Senator @BernieSanders at this afternoon's DNC meeting in Minneapolis. #nhpolitics",
            "Millenials are embracing @BernieSanders bc he sees student debt as national crisis http://t.co/T0CmHXU964 #students4bernie @Youth4Bern",
            "@usheredprogress @Women4Bernie @BernieSanders Please use his unedited version of the poster http://t.co/3WpY0kPTcq",
            "Jean Claude is the FIRST person to canvass Manchester with our ✨new✨ @HillaryClinton literature! #Hillary2016 http://t.co/ToEZ8Co4VY",
            "@americaNewsroom #MarthaMacCallum Huckster:FORCE raped 10 yo to have baby! Rubio:Let Women die! @GOP:*YAWN* @HillaryClinton: I'LL FIGHT4YOU!",
            "It's in the math, stupid, says @HillaryClinton's team. #FITN  https://t.co/sW5q4FrYAM",
            "@HaroldWNelson @jsc1835 #GOP #WarOnWomen Huck:Force raped 10 yo to have baby! Rubio:Let Women die! @GOP:*YAWN* @HillaryClinton:I'll FIGHT 4U",
            "@USATODAY @HillaryClinton Yawn...we still know about the poll: First word comes to mind thinking of Hillary=LIAR.",
            "@OutnumberedFNC #GOP #WarOnWomen Huck: Force RAPED 10yo to have baby! Rubio: Let Women DIE! @GOP:*YAWN*  @HillaryClinton: I'LL FIGHT FOR YOU",
            "@HillaryClinton Smart woman are running from you, you are a lying liar that lies. Pro-life doesn't equal terrorist. #tcot #StopHillary",
            "@OutnumberedFNC #outnumbered Who WHINES abt what @HillaryClinton said re: #GOP #WarOnWomen? 1. @GOP who hate her 2. Media who hates her lmao",
            "@OutnumberedFNC #outnumbered GOP is calling onHRC2apologize4defendingWOMEN in #GOP #WarOnWomen...  @HillaryClinton:I have a BUCKET list :)",
            "Gov. Walker at the Citadel in Charleston, SC: @HillaryClinton downplayed China’s abysmal human rights record. #2016 http://t.co/2oXac1Im9Q",
            "Another @HillaryClinton surrogate slated to swing through #FITN next week: @GovHowardDean, who will campaign in Keene Sept. 2.",
            "@cspan @HillaryClinton @TheDemocrats  This is really good. Thx!",
            "Are we safer now than we were 7 years ago? Anyone who believes the answer to this question is yes should vote for @HillaryClinton. -Walker",
            "Key @HillaryClinton progressive ally @HowardDean will campaign for her in Keene 9/2, college $ focus #fitn #nhpolitics  via @jdistaso",
            "Key @HillaryClinton progressive ally @HowardDean will campaign for her in Keene 9/2, college $ focus #fitn #nhpolitics #WMUR",
            "No matter who you are, what you look like, what religion you practice, or who love, America has a place for you -@HillaryClinton #FITN",
            "Uh, @HillaryClinton the VA TV shooter passed a background check. How would your universal check have made any difference? @Wes72971",
            "N.H.—If there’s any place where @HillaryClinton supporters should be feeling skittish, it’s here. #nhpolitics http://t.co/3IVLolJFgs",
            "@ScottWalker @HillaryClinton MASS IMMIGRATION BRINGS ISLAMIC TERRORISM TO THE USA -- IMMIGRATION MORATORIUM NOW!  #nhpolitics #alpolitics",
            "@ScottWalker @HillaryClinton HILLARY CLINTON &amp; HER HUMANITARIAN INTERVENTIONIST HARPIES ARE RESPONSIBLE FOR THE MESS IN LIBYA  #nhpolitics",
            "@BernieSanders @Alex_Penguin_3 Students Unite Come Join the Revolution http://t.co/sfX8imFlLR",
            "@DMBakes @BernieSanders Come join us in #WashingtonDC #EnoughIsEnough http://t.co/WzbJgQWXId",
            "Windham County supporters of @BernieSanders prepare to head across the river to campaign in #NH: http://t.co/Xa4S4rcauy",
            "Coming up shortly @MartinOMalley to address @TheDemocrats #nhpolitics http://t.co/Gnv7tFtWCp",
            "@Cspan @cspanwj Amazing when Ds r concerned abt HRC's OPTICS.  RUBIO: If this election becomes a RESUME contest @HillaryClinton wins",
            "@edhenry @AlexisinNH @SenSanders @HillaryClinton  If she got together with @RandPaul they may actually get a full crowd!",
            "Looking forward to watching @martinomalley address @thedemocrats. You can watch here http://t.co/A0y9s9MwHO #nhpolitics",
            "Hey @cspan not only will @MartinOMalley preserve social security, but he has a detailed plan to expand it https://t.co/VaN0hawXlI",
            "Ready for @MartinOMalley to take the stage at the @TheDemocrats summer meeting!",
            "An outline of the core issues being raised by @BernieSanders in his presidential campaign http://t.co/MSk4okbYeN #feelthebern #bernie2016",
            "@HillaryforNH Proud to be a part of this team, however small my role. We really want @HillaryClinton to win because this country needs her!",
            "Resounding applause for @MartinOMalley's calls for #weneeddebate at the @TheDemocrats summer meeting #nhpolitics",
            "We must stand up and speak out on the ideals that unite us -@MartinOMalley #WeneedDebate #nhpolitics",
            "This is not a time for silence -@MartinOMalley #WeneedDebate #nhpolitics",
            "National campaign chair for @HillaryClinton John Podesta headed to #fitn next week for forum on environment. 9/2 in Concord. #nhpolitics",
            "In MD @MartinOMalley passed the DREAM Act and Marriage Equality. We should be proud to talk about Dem ideals #WeNeedDebate #nhpolitics",
            "apparent preferential treatment received by @HillaryClinton was noted by state Democratic Party leaders http://t.co/tKfPG6IUwG #nhpolitics",
            "@elraymer @MartinOMalley Martin O'Malley made Maryland a sanctuary state for illegal alien invaders.     #nhpolitics @RefugeeWatcher #UKIP",
            "NH voters deserve more than 1 debate join @MartinOMalley &amp; tell @TheDemocrats to respect #fitn tradition #nhpolitics http://t.co/blRdb9I8qd",
            "What are we afraid of? - @MartinOMalley #weneeddebate #nhpolitics",
            "I just donated to @BernieSanders' campaign for president. Join me: http://t.co/FqlHClWUKu",
            "@HillaryClinton alternately fierce, funny, humorous brings cheering crowd to its feet, extols party unity #dems15  https://t.co/otuYX1EokC",
            "What a wonderful letter that just arrived in the mail! Thank you!  @HillaryClinton #FITN #nhpolitics HillaryforNH http://t.co/5yJZExs56b",
            "Our service to the cause of our country's better future demands it - @MartinOMalley #WeNeedDebate #nhpolitics https://t.co/YvrCC0EnzG",
            "@cspan @BernieSanders awesome, inspiring and truthful , as always. we DO need a revolution..",
            "#nhpolitics .I don't find it funny that.  @HillaryforNH is trying to poach me from the @BernieSanders campaign.",
            "When mass deportations &amp; axing birthrights of children are ideas of the hour #WeNeedDebate @MartinOMalley #nhpolitics http://t.co/iVx1DQqTss",
            "This +1,000,000,000 #WeNeedDebate #nhpolitics #fitn #omalleycrushedit cc: @MartinOMalley  https://t.co/HJ1rthpqjF",
            "@CNNPolitics @HillaryClinton They should be committed...to finding an honest candidate. HRC may be committed.../ indicted..UNWORTHY/Criminal",
            "I wish I had a FORTUNE to help @HillaryClinton out!  :(",
            "@HillaryClinton appears to have violated the Espionage Act. In fact 18 USC Section 793(f) https://t.co/1hMwUW9sF9  @DaTechGuyblog",
            "On #gunsense, @HillaryClinton, @MartinOMalley &amp; @LincolnChafee, are making the grade. @BernieSanders and @JimWebbUSA, not so much. #fitn",
            "@HillaryClinton @Lg4Lg For our children? We don't have children. I have children and you and your liberal trolls will not get near them.",
            "@HillaryClinton @Lg4Lg Like you championed for your friend and colleague Chris Stevens? Yeah great job.",
            "@edhenry @jjauthor @HillaryClinton the same unstable , mentally ill lunatics that support the liar in the Whitehouse.",
            "@lowkeynokey @Scattermae777M @Lg4Lg @HillaryClinton When it comes to comparisons to the Nazis the Democrats take the lead.",
            "Stricter Gun Control, NOT, 2nd Amendment Revoked, must be addressed in 2016... @HillaryClinton @SenSchumer @SenWarren @katiecouric",
            "Mental Illness And Guns do NOT mix! @HillaryClinton @enoreikaTV @PaulaEbbenWBZ @RainPryor @kelly_carlin @ElizabethBanks",
            "Stricter Gun Control MUST Be Addressed in Election Year 2016!!! @HillaryClinton @ERIKAjaneC @LegendaryWriter @ElizabethBanks @thumbfighter",
            "@LynnHergenrader @BernieSanders shes been at it for about a month.",
            "@LynnHergenrader @BernieSanders pretty good! Hope you have a great weekend!🌹☺",
            "@nytopinion @BernieSanders A nice article on Bernie from the NY Times is refreshing.",
            "@realDonaldTrump either a plant for @TedCruz @JebBush or his dear friend @HillaryClinton http://t.co/WDB4BqU82o",
            "As more people listen to @BernieSanders , more people support him.  Join me in nominating @SenSanders for President https://t.co/XP9r0CpIeS",
            "Coming soon to bookstores - The Essential @BernieSanders and His Vision for America. Read the book, #FeelTheBern http://t.co/TcmDoh8EHH",
            "@BernieSanders well actually it does..it is EXTREMELY profitable for oligarchs &amp; schools themselves....legalized looting of peasntry",
            "@HarrietBaldwin @AgnesClaire #hillarynh  MT-&gt;@Hillarysserver @HillaryClinton and sycophats have you seen this? https://t.co/LdtNNx5FdF h/t",
            "@BernieSanders I'm all over this! #FeelTheBern run, Bernie, run!",
            "Great crowd for our #fitn canvass here in Manchester for @HillaryClinton!! #nhpolitics #Hillary2016 http://t.co/GIKvSvGBEI",
            "And let me tell you one thing. She's a fighter. Oh god, is she a fighter. -Congressman McGovern on @HillaryClinton http://t.co/wuvIuKFzsi",
            "Strong speech here from @BernieSanders to @TheDemocrats http://t.co/RRO7oYA7tz",
            "Fantastic speech!   @HillaryClinton knocks it out of the park at DNC yesterday!  http://t.co/V506lVqBUl  #nhpolitics",
            "Tell @HillaryClinton @BernieSanders @MartinOMalley: Pledge to pick a VP who supports the #IranDeal http://t.co/fXRgZU5TxU via @CREDOMobile",
            "@ouchinagirl @LeahR77 @HillaryClinton The Clinton Fund got $5 million from Gilbert Chagoury when Hillary didn't libel the Boko Harem boys.",
            "Hurricane Katrina anniversary hits home for @MartinOMalley  via  @Newsweek http://t.co/OZg1Yncftd #nhpolitics",
            "Proud to be at Keene State talking to young voters for @MartinOMalley http://t.co/VddjXNhh9p",
            "@HillaryClinton is out of touch http://t.co/6X5FfvbMUm",
            "@ffweekend @FoxNews @HillaryClinton Anything to get out from under email questions &amp; Ed Henry.",
            "Check it out--&gt; @maura_healey got her FIRST Commit to Vote Card 4 @HillaryClinton knocking on doors w/ @HillaryforNH http://t.co/Cx7vGNtoyV",
            "@JaneAnneJ @jojokejohn @HillaryClinton @HuffPostPol that is such a cheap irrelevant shot.",
            "@JaneAnneJ @jojokejohn @HillaryClinton @HuffPostPol wheres Lindsey s “ significant other”..pray tell?"
        });

        static IEnumerator<string> _enumerableList = _linkedList.GetEnumerator();

        #endregion

        #region Static members

        static object _syncCallback = new object();

        #endregion

        #region Constructors

        public InfiniteDataProvider()
        {
        }

        #endregion

        #region IDataProvider implementation

        public bool HasData
        {
            get
            {
                return true;
            }
        }

        public bool CanReschedule
        {
            get
            {
                return false;
            }
        }

        public bool SupportBatches
        {
            get
            {
                return true;
            }
        }

        public int CharactersLimit
        {
            get;
            set;
        }

        public bool Initialize(params object[] args)
        {
            return true;
        }

        public List<dynamic> ReadBatch(int size)
        {
            List<dynamic> batch = new List<dynamic>(size);

            for (int i = 0; i < size; i++)
            {
                dynamic document = ReadNext();
                if (document == null)
                {
                    return batch;
                }

                batch.Add(document);
            }

            return batch;
        }

        public dynamic ReadNext()
        {
            if (!_enumerableList.MoveNext())
            {
                _enumerableList.Reset();
                return ReadNext();
            }

            dynamic document = new ExpandoObject();
            document.id = Guid.NewGuid().ToString();
            document.text = _enumerableList.Current;

            return document;
        }

        public bool Reschedule(dynamic record)
        {
            throw new NotImplementedException();
        }

        public bool RescheduleBatch(List<dynamic> batch)
        {
            throw new NotImplementedException();
        }

        #endregion
    }
}
