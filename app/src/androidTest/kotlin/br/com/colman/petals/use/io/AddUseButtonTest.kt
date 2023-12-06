import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import io.mockk.mockk
import br.com.colman.petals.use.AddUseButton
import br.com.colman.petals.use.pause.repository.Pause
import br.com.colman.petals.use.repository.UseRepository
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.time.LocalTime

@RunWith(JUnit4::class)
class AddUseButtonTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun AddUseButton_visible_when_not_in_pause() {
    val mockUseRepository = mockk<UseRepository>(relaxed = true)
    composeTestRule.setContent {
      AddUseButton(repository = mockUseRepository)
    }
    composeTestRule.onNodeWithText("Add Use").assertIsDisplayed()
    composeTestRule.onNodeWithText("Add Use").performClick()
    composeTestRule.onNodeWithText("OK").assertIsDisplayed()
  }

  @Test
  fun AddUseButton_clickable_when_not_in_pause() {
    val mockUseRepository = mockk<UseRepository>(relaxed = true)
    composeTestRule.setContent {
      AddUseButton(repository = mockUseRepository)
    }
    composeTestRule.onNodeWithText("Add Use").assertIsDisplayed()
    composeTestRule.onNodeWithText("Add Use").performClick()
    composeTestRule.onNodeWithText("OK").assertIsDisplayed()
    composeTestRule.onNodeWithText("Date").assertIsDisplayed()

  }

  @Test
  fun AddUseButton_dialog_dissapears_on_ok() {
    val mockUseRepository = mockk<UseRepository>(relaxed = true)
    composeTestRule.setContent {
      AddUseButton(repository = mockUseRepository)
    }

    composeTestRule.onNodeWithText("Add Use").assertIsDisplayed()
    composeTestRule.onNodeWithText("Add Use").performClick()
    composeTestRule.onNodeWithText("OK").assertIsDisplayed()
    composeTestRule.onNodeWithText("OK").performClick()
    composeTestRule.onNodeWithText("Add Use").assertIsDisplayed()
  }

  @Test
  fun AddUseButton_calling_repository() {
    val mockUseRepository = mockk<UseRepository>(relaxed = true)
    composeTestRule.setContent {
      AddUseButton(repository = mockUseRepository)
    }
    composeTestRule.onNodeWithText("Add Use").assertIsDisplayed()
    composeTestRule.onNodeWithText("Add Use").performClick()
    composeTestRule.onNodeWithText("OK").assertIsDisplayed()
    composeTestRule.onNodeWithText("OK").performClick()
    verify(exactly = 1) { mockUseRepository.upsert(any()) }
  }

  @Test
  fun AddUseButton_visible_when_in_pause() {
    val mockUseRepository = mockk<UseRepository>(relaxed = true)
    val invalidPause = Pause(
      startTime = LocalTime.of(8, 0),
      endTime = LocalTime.of(12, 0)
    )
    composeTestRule.setContent {
      AddUseButton(repository = mockUseRepository, pause = invalidPause)
    }
    composeTestRule.onNodeWithText("Add Use").assertIsDisplayed()
  }

  @Test
  fun AddUseButton_does_not_open_dialog_when_in_pause() {
    val mockUseRepository = mockk<UseRepository>(relaxed = true)
    val invalidPause = Pause(
      startTime = LocalTime.of(8, 0),
      endTime = LocalTime.of(12, 0)
    )
    composeTestRule.setContent {
      AddUseButton(repository = mockUseRepository, pause = invalidPause)
    }
    composeTestRule.onNodeWithText("Add Use").assertIsDisplayed()
    composeTestRule.onNodeWithText("Add Use").performClick()
    composeTestRule.onNodeWithText("Date").assertDoesNotExist()
  }

  @Test
  fun AddUseButton_opens_alert_when_in_pause() {
    val mockUseRepository = mockk<UseRepository>(relaxed = true)
    val invalidPause = Pause(
      startTime = LocalTime.of(8, 0),
      endTime = LocalTime.of(12, 0)
    )
    composeTestRule.setContent {
      AddUseButton(repository = mockUseRepository, pause = invalidPause)
    }
    composeTestRule.onNodeWithText("Add Use").assertIsDisplayed()
    composeTestRule.onNodeWithText("Add Use").performClick()
    composeTestRule.onNodeWithText("You scheduled this period to be a pause. Are you sure you want to add a use now?")
      .assertIsDisplayed()
  }

  @Test
  fun AddUseButton_yes_not_clickable_firstly_when_in_pause() {
    val mockUseRepository = mockk<UseRepository>(relaxed = true)
    val invalidPause = Pause(
      startTime = LocalTime.of(12, 0),
      endTime = LocalTime.of(8, 0)
    )
    composeTestRule.setContent {
      AddUseButton(repository = mockUseRepository, pause = invalidPause)
    }
    composeTestRule.onNodeWithText("Add Use").assertIsDisplayed()
    composeTestRule.onNodeWithText("Add Use").performClick()
    composeTestRule.onNodeWithText("Yes").assertDoesNotExist()
  }

  @Test
  fun AddUseButton_yes_clickable_afrer_10s_when_in_pause() {
    val mockUseRepository = mockk<UseRepository>(relaxed = true)
    val invalidPause = Pause(
      startTime = LocalTime.of(8, 0),
      endTime = LocalTime.of(12, 0)
    )
    composeTestRule.setContent {
      AddUseButton(repository = mockUseRepository, pause = invalidPause)
    }
    composeTestRule.onNodeWithText("Add Use").assertIsDisplayed()
    composeTestRule.onNodeWithText("Add Use").performClick()
    composeTestRule.waitForIdle()
    val endTimeMillis = System.currentTimeMillis() + 11000
    while (System.currentTimeMillis() < endTimeMillis) {
      composeTestRule.waitForIdle()
//      delay(10000)
    }
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithText("No").assertExists()
  }

  @Test
  fun AddUseButton_calendar_opens_when_not_in_pause() {
    val mockUseRepository = mockk<UseRepository>(relaxed = true)
    composeTestRule.setContent {
      AddUseButton(repository = mockUseRepository)
    }
    composeTestRule.onNodeWithText("Add Use").assertIsDisplayed()
    composeTestRule.onNodeWithText("Add Use").performClick()
    composeTestRule.onNodeWithText("OK").assertIsDisplayed()
    composeTestRule.onNodeWithText("Date").assertIsDisplayed()
    composeTestRule.onNodeWithText("Date").performClick()
    composeTestRule.onNodeWithText("Select Date").assertIsDisplayed()
  }

}
