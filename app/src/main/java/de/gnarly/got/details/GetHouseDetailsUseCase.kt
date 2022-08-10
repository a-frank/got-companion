package de.gnarly.got.details

import de.gnarly.got.model.House
import de.gnarly.got.repository.GoTRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
class GetHouseDetailsUseCase @Inject constructor(
	private val gotRepository: GoTRepository
) {
	operator fun invoke(id: Int): Flow<House> =
		gotRepository.getHouseFlow(id)
			.flatMapLatest { house ->
				gotRepository.getNameOfTheCurrentLord(house.currentLord)
					.map { lord ->
						house to lord
					}
			}.map { (house, lord) ->
				house.copy(currentLord = lord)
			}
}