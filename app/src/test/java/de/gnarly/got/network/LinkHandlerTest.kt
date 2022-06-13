package de.gnarly.got.network

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class LinkHandlerTest {
	@Test
	fun `get page number of next link`() {
		val expected = 2

		val handler = LinkHandler()
		val links =
			"<https://anapioficeandfire.com/api/houses?page=$expected&pageSize=10>; rel=\"next\", <https://anapioficeandfire.com/api/houses?page=1&pageSize=10>; rel=\"first\", <https://anapioficeandfire.com/api/houses?page=45&pageSize=10>; rel=\"last\""

		val nextPage = handler.getNextPage(links)

		assertThat(nextPage).isNotNull()
		assertThat(nextPage).isEqualTo(expected)
	}

	@Test
	fun `get null without next link`() {
		val handler = LinkHandler()
		val links = "<https://anapioficeandfire.com/api/houses?page=1&pageSize=10>; rel=\"first\", <https://anapioficeandfire.com/api/houses?page=45&pageSize=10>; rel=\"last\""

		val nextPage = handler.getNextPage(links)
		assertThat(nextPage).isNull()
	}

	@Test
	fun `get null on invalid next link`() {
		val handler = LinkHandler()
		val links = "<https://anapioficeandfire.com/api/houses?pageSize=10>; rel=\"next\", <https://anapioficeandfire.com/api/houses?page=1&pageSize=10>; rel=\"first\""

		val nextPage = handler.getNextPage(links)
		assertThat(nextPage).isNull()
	}
}