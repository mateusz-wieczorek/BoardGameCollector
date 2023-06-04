package edu.put.inf151312

class Game{
    var id: Int? = 0
    var title: String? = null
    var year: Int? = 0
    var image: String? = null
    var bgg_id: Long? = null

    constructor( title: String?, year: Int?,image: String?,bgg_id: Long?){

        this.title=title
        this.year = year
        this.image = image
        this.bgg_id = bgg_id
    }
    constructor(id: Int?,title: String?, year: Int?,image: String?,bgg_id: Long?){
        this.id = id
        this.title=title
        this.year = year
        this.image = image
        this.bgg_id = bgg_id
    }
}