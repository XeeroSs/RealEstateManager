package com.openclassrooms.realestatemanager

class PropertyModel(var typeProperty: String,
                    var priceDollarProperty: String,
                    var surfaceProperty: Int,
                    var pieceNumberProperty: Int,
                    var descriptionProperty: String,
                    var addressProperty: String,
                    var interestPointProperty: Array<String>,
                    var statusProperty: Boolean,
                    var dateProperty: String,
                    var saleDateProperty: String,
                    var realEstateAgentProperty: String,
                    var photosProperty: HashMap<String, String>)