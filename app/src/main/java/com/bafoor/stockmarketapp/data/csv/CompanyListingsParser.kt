package com.bafoor.stockmarketapp.data.csv

import com.bafoor.stockmarketapp.domain.model.CompanyListing
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyListingsParser @Inject constructor() :CSVParser<CompanyListing> {

    override suspend fun parse(inputStream: InputStream): List<CompanyListing> {
        val csvReader = CSVReader(InputStreamReader(inputStream))
        return withContext(Dispatchers.IO) {
            csvReader
                .readAll()
                .drop(1) // <-- first row of csv file descripe what column contain
                .mapNotNull { line ->
                    val symbol = line.getOrNull(0)
                    val name = line.getOrNull(1)
                    val exchange = line.getOrNull(2)
                    CompanyListing(
                        name = name ?: return@mapNotNull null,
                        symbol = symbol ?: return@mapNotNull null,
                        exchange = exchange ?: return@mapNotNull null
                    )
                } .also {
                    csvReader.close()
                }
        }
    }
}
















