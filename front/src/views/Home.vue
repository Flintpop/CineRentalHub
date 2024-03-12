<template>
  <div class="home">
    <!-- Navbar -->
    <Navbar/>

    <!-- Bannière principale -->
    <section class="banner">
      <h1>CineRentalHub</h1>
      <p>Découvrez | Louez | Partagez</p>
    </section>

    <!-- Section Films -->
    <section class="films">
      <h2>Nos Films</h2>
      <div class="film-list">
        <movies_list :films="films"></movies_list>
      </div>
    </section>

    <!-- Section À propos -->
    <section class="about">
      <h2>À propos de CineRentalHub</h2>
      <p>Nous sommes votre destination ultime pour la location de films en ligne. Parcourez une vaste sélection de
        films, louez vos favoris et partagez vos avis !</p>
    </section>

    <!-- Section Contact -->
    <!-- Section Contact -->
    <section class="contact">
      <h2>Contactez-nous</h2>
      <p>Des questions ou des suggestions ? Nous sommes à votre écoute ! Contactez-nous.</p>
      <p>Email: contact@CineRentalHub.com</p>
    </section>

    <!-- Pied de page -->
    <Footer/>
  </div>
</template>

<script>
import Navbar from '../components/Navbar.vue';
import Footer from "../components/Footer.vue";
import movies_list from "../components/movies_list.vue";

export default {
  name: 'Home',
  components: {
    Footer,
    Navbar,
    movies_list
  },

  mounted() {
    if (localStorage.getItem('userId')) {
      this.$router.push('/homeConnected');
    }
    // Simuler la récupération de données
    this.fetchMovies();
  },

  methods: {
    fetchMovies() {
      // Simulation de la récupération de données depuis la base de données
      this.films = [
        {
          id: 1,
          available: true,
          title: "AVATAR",
          release_date: "2009-12-16",
          daily_rental_price: 2.99,
          purchase_price: 9.99,
          description: "Malgré sa paralysie, Jake Sully, un ancien marine immobilisé dans un fauteuil roulant, est resté un combattant au plus profond de son être. Il est recruté pour se rendre à des années-lumière de la Terre, sur Pandora, où de puissants groupes industriels exploitent un minerai rarissime destiné à résoudre la crise énergétique sur Terre. Parce que l'atmosphère de Pandora est toxique pour les humains, ceux-ci ont créé le Programme Avatar, qui permet à des \" pilotes \" humains de lier leur esprit à un avatar, un corps biologique commandé à distance, capable de survivre dans cette atmosphère létale. Ces avatars sont des hybrides créés génétiquement en croisant l'ADN humain avec celui des Na'vi, les autochtones de Pandora.",
          link: "https://m.media-amazon.com/images/I/51EUk47IF8L._AC_SL1080_.jpg"
        },
        {
          id: 2,
          available: true,
          title: "INCEPTION",
          release_date: "2010-07-16",
          daily_rental_price: 3.99,
          purchase_price: 12.99,
          description: "Dom Cobb est un voleur expérimenté – le meilleur qui soit dans l'art périlleux de l'extraction : sa spécialité consiste à s'approprier les secrets les plus précieux d'un individu, enfouis au plus profond de son subconscient, pendant qu'il rêve et que son esprit est particulièrement vulnérable. Très recherché pour ses talents dans l'univers trouble de l'espionnage industriel, Cobb est également devenu un fugitif traqué dans le monde entier qui a perdu tout ce qui lui est cher. Mais une ultime mission pourrait lui permettre de retrouver sa vie d'avant – à condition qu'il puisse accomplir l'impossible : l'inception. Au lieu de subtiliser un rêve, Cobb et son équipe doivent faire l'inverse : implanter une idée dans l'esprit d'un individu. S'ils y parviennent, cela pourrait être le crime parfait.",
          link: "https://m.media-amazon.com/images/I/91b3Xtjt0IL._AC_SL1500_.jpg"
        },
        {
          id: 3,
          available: true,
          title: "THE DARK KNIGHT",
          release_date: "2008-07-18",
          daily_rental_price: 2.99,
          purchase_price: 9.99,
          description: "Dans ce nouveau volet, Batman augmente les mises dans sa guerre contre le crime. Avec l'appui du lieutenant de police Jim Gordon et du procureur de Gotham, Harvey Dent, Batman vise à éradiquer le crime organisé qui pullule dans la ville. Leur association est très efficace, mais elle sera bientôt bouleversée par le chaos déclenché par un criminel extraordinaire que les citoyens de Gotham connaissent sous le nom de Joker.",
          link: "https://www.affichescinema.fr/612-large_default/the-dark-knight-affiche-40x60cm.jpg"
        },
        {
          id: 4,
          available: true,
          title: "THE MATRIX",
          release_date: "1999-06-23",
          daily_rental_price: 1.99,
          purchase_price: 7.99,
          description: "Programmeur anonyme dans un service administratif le jour, Thomas Anderson devient Neo la nuit venue. Sous ce pseudonyme, il est l'un des pirates les plus recherchés du cyber-espace. À cheval entre deux mondes, Neo est assailli par d'étranges songes et des messages cryptés provenant d'un certain Morpheus. Celui-ci l'exhorte à aller au-delà des apparences et à trouver la réponse à la question qui hante constamment ses pensées : qu'est-ce que la Matrice ? Nul ne le sait, et aucun homme n'est encore parvenu à en percer les defenses. Mais Morpheus est persuadé que Neo est l'Elu, le libérateur mythique de l'humanité annoncé selon la prophétie. Ensemble, ils se lancent dans une lutte sans retour contre la Matrice et ses terribles agents...",
          link: "https://i.ebayimg.com/images/g/zwAAAOSwCGVX5wN4/s-l1200.webp"
        },
        {
          id: 5,
          available: true,
          title: "STAR WARS: EPISODE IV - A NEW HOPE",
          release_date: "1977-05-25",
          daily_rental_price: 1.99,
          purchase_price: 7.99,
          description: "Il y a bien longtemps, dans une galaxie très lointaine... La guerre civile fait rage entre l'Empire galactique et l'Alliance rebelle. Capturée par les troupes de choc de l'Empereur menées par le sombre et impitoyable Dark Vador, la princesse Leia Organa dissimule les plans de l'Etoile Noire, une station spatiale invulnérable, à son droïde R2-D2 avec pour mission de les remettre au Jedi Obi-Wan Kenobi. Accompagné de son fidèle compagnon, le droïde de protocole C-3PO, R2-D2 s'échoue sur la planète Tatooine et termine sa quête chez le jeune Luke Skywalker. Rêvant de devenir pilote mais confiné aux travaux de la ferme, ce dernier se lance à la recherche de ce mystérieux Obi-Wan Kenobi, devenu ermite au coeur des montagnes désertiques de Tatooine...",
          link: "https://bananaroad.com/cdn/shop/products/pst1476sthpe_large.jpg?v=1485544998"
        },
      ];
    }
  },

  data() {
    return {
      films: [],
    };
  },
};
</script>

<style scoped>
/* Styles adaptés pour la nouvelle thématique */
.banner, .about, .contact {
  padding: 40px 20px;
  text-align: center;
}

.films, .about, .contact {
  padding: 20px;
}

footer {
  background-color: #f8f9fa;
  text-align: center;
  padding: 20px;
  margin-top: 40px;
}
</style>