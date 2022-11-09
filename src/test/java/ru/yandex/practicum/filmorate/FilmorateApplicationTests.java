package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.mpaandgenres.MpaAndGenresStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FilmorateApplicationTests {

	private final UserStorage userStorage;

	private final FriendshipStorage friendshipStorage;

	private final FilmStorage filmStorage;

	private final LikesStorage likesStorage;

	private final MpaAndGenresStorage mpaAndGenresStorage;

	@BeforeAll
	void addCorrectUsersAndFilms() {
		User user = new User();
		user.setName("Test Name");
		user.setLogin("testlogin");
		user.setEmail("test@test.ru");
		user.setBirthday(LocalDate.of(2005, 12, 5));
		User user2 = new User();
		user2.setName("Test Name 2");
		user2.setLogin("testlogin2");
		user2.setEmail("test2@test2.ru");
		user2.setBirthday(LocalDate.of(2004, 11, 6));
		User user3 = new User();
		user3.setName("Test Name 3");
		user3.setLogin("testlogin3");
		user3.setEmail("test3@test3.ru");
		user3.setBirthday(LocalDate.of(2003, 10, 4));
		userStorage.addUser(user);
		userStorage.addUser(user2);
		userStorage.addUser(user3);
		Film film = new Film();
		film.setName("Test Film");
		film.setDescription("Test Discription");
		film.setDuration(100);
		film.setReleaseDate(LocalDate.of(1994,12,27));
		film.setRate(4);
		film.setMpa(new Mpa(2, "PG"));
		film.setGenres(new HashSet<>(Arrays.asList(new Genre(1, "Комедия"), new Genre(2, "Драма"))));
		Film film2 = new Film();
		film2.setName("Test Film 2");
		film2.setDescription("Test Discription 2");
		film2.setDuration(90);
		film2.setReleaseDate(LocalDate.of(1998,2,3));
		film2.setRate(3);
		film2.setMpa(new Mpa(1, "G"));
		film2.setGenres(new HashSet<>(Arrays.asList(new Genre(3, "Мультфильм"), new Genre(4, "Триллер"))));
		Film film3 = new Film();
		film3.setName("Test Film 3");
		film3.setDescription("Test Discription 3");
		film3.setDuration(90);
		film3.setReleaseDate(LocalDate.of(1996,5,3));
		film3.setRate(3);
		film3.setMpa(new Mpa(3, "PG-13"));
		film3.setGenres(new HashSet<>(Arrays.asList(new Genre(2, "Драмма"), new Genre(3, "Мультфильм"))));
		filmStorage.addFilm(film);
		filmStorage.addFilm(film2);
		filmStorage.addFilm(film3);
	}

	@Test
	public void getUserListTest() {
		assertEquals(3, userStorage.findAll().size());
	}

	@Test
	public void getUserByIdTest() {
		User userTestById = new User();
		userTestById.setId(2);
		userTestById.setName("Test Name 2");
		userTestById.setLogin("testlogin2");
		userTestById.setEmail("test2@test2.ru");
		userTestById.setBirthday(LocalDate.of(2004, 11, 6));
		assertEquals(userTestById, userStorage.findById(2));
	}

	@Test
	public void updateUserTest() {
		User userUpdate = new User();
		userUpdate.setId(3);
		userUpdate.setName("Update Name");
		userUpdate.setLogin("updatelogin");
		userUpdate.setEmail("updatetest@test.ru");
		userUpdate.setBirthday(LocalDate.of(2005, 12, 5));
		userStorage.updateUser(userUpdate);
		assertEquals(userUpdate, userStorage.findById(3));
	}

	@Test
	public void addAndDeleteUserTest() {
		User user = new User();
		user.setName("Add Test");
		user.setLogin("AddTest");
		user.setBirthday(LocalDate.of(1992, 06, 15));
		user.setEmail("addtest@test.ru");
		Integer userListSize = userStorage.findAll().size();
		userStorage.addUser(user);
		user.setId(userListSize + 1);
		assertEquals(user, userStorage.findById(userListSize + 1));
		userStorage.deleteUser(userListSize + 1);
		assertEquals(userListSize, userStorage.findAll().size());
	}

	@Test
	public void addAndDeleteFriendTest() {
		friendshipStorage.addFriend(1, 2);
		assertEquals(1, friendshipStorage.getFriendList(1).size());
		friendshipStorage.deleteFriend(1, 2);
		assertEquals(0, friendshipStorage.getFriendList(1).size());
	}

	@Test
	public void getCommonFriendsTest() {
		friendshipStorage.addFriend(1, 2);
		friendshipStorage.addFriend(3, 2);
		assertEquals(userStorage.findById(2), friendshipStorage.commonFriendsList(1, 3).get(0));
		friendshipStorage.deleteFriend(1, 2);
		friendshipStorage.deleteFriend(3, 2);
	}


	@Test
	public void getFilmListTest() {
		assertEquals(3, filmStorage.findAll().size());
	}

	@Test
	public void getFilmByIdTest() {
		Film filmTestById = new Film();
		filmTestById.setId(2);
		filmTestById.setName("Test Film 2");
		filmTestById.setDescription("Test Discription 2");
		filmTestById.setDuration(90);
		filmTestById.setReleaseDate(LocalDate.of(1998,2,3));
		filmTestById.setRate(3);
		filmTestById.setMpa(new Mpa(1, "G"));
		filmTestById.getMpa().setName("G");
		List<Genre> genresOfFilm = new ArrayList<>();
		genresOfFilm.add(new Genre(4, "Триллер"));
		genresOfFilm.add(new Genre(3, "Мультфильм"));
		filmTestById.setGenres(new HashSet<>(genresOfFilm));
		assertEquals(filmTestById, filmStorage.findById(2));
	}

	@Test
	public void addAdnDeleteFilmTest() {
		Film film = new Film();
		film.setName("Add Film Test");
		film.setDescription("Add Test Discription");
		film.setDuration(100);
		film.setReleaseDate(LocalDate.of(1994,12,27));
		film.setRate(4);
		film.setMpa(new Mpa(2, "PG"));
		film.setGenres(new HashSet<>(Arrays.asList(new Genre(1, "Комедия"), new Genre(2, "Драма"))));
		Integer filmListSize = filmStorage.findAll().size();
		filmStorage.addFilm(film);
		film.setId(4);
		assertEquals(film, filmStorage.findById(4));
		filmStorage.deleteFilm(4);
		assertEquals(filmListSize, filmStorage.findAll().size());
	}

	@Test
	public void updateFilmTest() {
		Film updatedFilm = new Film();
		updatedFilm.setId(3);
		updatedFilm.setName("Updated Film");
		updatedFilm.setDescription("Updated Discription");
		updatedFilm.setDuration(90);
		updatedFilm.setReleaseDate(LocalDate.of(1996,5,3));
		updatedFilm.setRate(3);
		updatedFilm.setMpa(new Mpa(2, "PG"));
		filmStorage.updateFilm(updatedFilm);
		assertEquals(updatedFilm, filmStorage.findById(3));
	}

	@Test
	public void addAndDeleteLikeTest() {
		Integer actualRate = filmStorage.findById(1).getRate();
		likesStorage.addLike(1, 1);
		assertEquals(actualRate + 1, filmStorage.findById(1).getRate());
		likesStorage.deleteLike(1, 2);
		assertEquals(actualRate, filmStorage.findById(1).getRate());
	}

	@Test
	public void getPopularFilmTest() {
		likesStorage.addLike(3, 1);
		likesStorage.addLike(3, 2);
		assertEquals(filmStorage.findById(3), filmStorage.getPopularFilms(3).get(0));
		likesStorage.deleteLike(3, 1);
		likesStorage.deleteLike(3, 2);
	}

	@Test
	public void findMpaById() {
		Mpa mpa = new Mpa(2, "PG");
		assertEquals(mpa, mpaAndGenresStorage.findMpaById(2));
	}

	@Test
	public void findAllMpa() {;
		assertEquals(5, mpaAndGenresStorage.findAllMpa().size());
	}

	@Test
	public void findGenreById() {
		Genre genre = new Genre(4, "Триллер");
		assertEquals(genre, mpaAndGenresStorage.findGenreById(4));
	}

	@Test
	public void findAllGenres() {;
		assertEquals(6, mpaAndGenresStorage.findAllGenres().size());
	}


}
