
const app = Vue.createApp({
    data() {
        return {
            clients:{},
            cardsDebits:[],
            cardsCredits:[],
            cards:[],
            cardholder:'',
            thruDate:'',
            cardColor:'',
            cardType:'',
            CardId:'',
            cardsFilter:[],
            fromDate:'',
            today:[],
            email: '',
        }
    },

    created() {
        axios.get('/api/clients/current')
        .then((response) =>{
            this.clients = response.data;
            this.cards = this.clients.cards;
            this.fromDate = this.cards.filter(card => card.fromDate)
            console.log(this.fromDate)
            this.today = new Date().toISOString(),
            this.email = this.clients.email; 
            this.cardsFilter = this.cards.filter(card => card.active == true);
            this.cardsDebits = this.cards.filter((a)=>{
                return a.cardType == "DEBIT"
            })
            this.cardsCredits = this.cards.filter((a)=>{
                return a.cardType == "CREDIT"
            })
        })
        .catch(function (error) {
            console.log(error);
        });
    },
    methods:{
        logOut(){
            axios.post('/api/logout').
            then(response => location.href = '/web/index.html')
        },
        deleteCard(card){
            this.CardId = card.id
            axios.patch('/api/clients/current/cards/delete' , `CardId=${this.CardId}`)
            .then(                
                response => swal({
                title: "Card deleted successfully" ,
                icon: "success",
                button: "Accept",
            }))
            .then(response => window.location.reload())
            .catch(error =>
                swal({
                    icon: 'error',
                    title: 'Oops...',
                    text: error.response.data,
        }))
        },
        expiredCard(){

        }

    }

}).mount('#app')


