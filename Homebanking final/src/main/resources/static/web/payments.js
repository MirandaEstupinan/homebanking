const app = Vue.createApp({
    data() {
        return {
            cardId: '',
            cardHolder: '',
            number: '',
            amount: '',
            cvv: '',
            thruDate: '',
            description: '',
            cards: '',
            clients: '',
            email: '',
        }
    },

    created() {
        axios.get('/api/clients/current')
            .then((response) => {
                this.clients = response.data;
                this.cards = this.clients.cards;
                this.fromDate = this.cards.filter(card => card.fromDate)
                console.log(this.fromDate)
                this.email = this.clients.email;
                this.today = new Date().toISOString(),
                    console.log(this.date)
                this.cardsFilter = this.cards.filter(card => card.active == true);
                this.cardsDebits = this.cards.filter((a) => {
                    return a.cardType == "DEBIT"
                })
                this.cardsCredits = this.cards.filter((a) => {
                    return a.cardType == "CREDIT"
                })
            })

    },
    methods: {
        newPayment() {
            axios.post("/api/clients/current/transactions/payments",
                {
                    "id": `${this.cardId}`,
                    "cardHolder": `${this.cardHolder}`,
                    "number": `${this.number}`,
                    "amount": `${this.amount}`,
                    "cvv": `${this.cvv}`,
                    "thruDate": `${this.thruDate}`,
                    "description": `${this.description}`
                })
                .then(response => swal({
                    title: "Successful payment",
                    icon: "success",
                    button: "Accept",
                }))

                .catch(error => swal({
                    icon: 'error',
                    title: 'Oops...',
                    text: error.response.data,
                }))
        },
        logOut() {
            axios.post('/api/logout').
                then(response => window.location.href = '/web/index.html')
        },

    },

}

).mount('#app')



