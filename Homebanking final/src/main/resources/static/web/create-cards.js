const app = Vue.createApp({
    data() {
        return {
            clients:[],
            cards:[],
            cardsCredits: [],
            cardsDebits:[],
            cardType: '',
            cardColor: '',
            clients: '',
            email:'',
            accountOrigin:'',
            accounts:'',
        }
    },

    created() {
        axios.get('/api/clients/current')
        .then(response => {
            this.clients = response.data,
            this.accounts = this.clients.accounts,
            this.email = this.clients.email}
            )
    },
    methods: {
        newCard() {
            axios.post("/api/clients/current/cards", `cardType=${this.cardType}&cardColor=${this.cardColor}&accountOrigin=${this.accountOrigin}`,
                { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(response => swal({
                    title: "Card Created successfully" ,
                    icon: "success",
                    button: "Accept",
                }))
                .then(response => location.href = '/web/cards.html')
                .catch(error =>
                    swal({
                        icon: 'error',
                        title: 'Oops...',
                        text: error.response.data,
            }))
        },
        logOut(){
            axios.post('/api/logout').
            then(response => location.href = '/web/index.html')
        },
    }

}).mount('#app')