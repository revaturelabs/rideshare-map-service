package com.revature.rideforce.maps.services;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.maps.GeoApiContext;
import com.netflix.discovery.shared.Application;
import com.revature.rideforce.maps.beans.Route;
import com.revature.rideforce.maps.configuration.TestConfiguration;
import com.revature.rideforce.maps.service.RouteService;


@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@Configuration
@ComponentScan(basePackages = {"com.revature.rideforce.maps.service"})
public class RouteServiceTest {
	
//	@Autowired
	@MockBean
	GeoApiContext geoApiContext;
	//static private before
	
	@MockBean
//	@Autowired
	RouteService routeService;
	
	
//	real values
	public RouteService routeService1= new RouteService();
	public GeoApiContext realGeo;
	public RouteService routeService2= new RouteService(realGeo);
	
	@Before
	public void validate() {
//		final RouteService routeService1= new RouteService();
//		final GeoApiContext realGeo = null;
//		final RouteService routeService2= new RouteService(realGeo);
		assertNotNull(routeService);
		Assert.assertThat(routeService, instanceOf(RouteService.class));
	}
	
	@Test
	public void nullTest() {
		assertNotNull(routeService1);
		assertNotNull(routeService2);
//		assertNotNull(routeService2.getGeoApiContext());
		
//		assertNotNull(realGeo);
	}
	
	
	@Test
	public void getGeoApi() {
		routeService.setGeoApiContext(geoApiContext);
		given(routeService.getGeoApiContext()).willReturn(geoApiContext);
		//use instantiated route service
		GeoApiContext geo=routeService.getGeoApiContext();
		Assert.assertThat(geo,instanceOf(GeoApiContext.class));
}
	
	@Test
	public void goodRoute(){
//		final RouteService routeService1= new RouteService(geoApiContext);
		routeService.setGeoApiContext(geoApiContext);
//		routeService1.setGeoApiContext(geoApiContext);
		final String start = "2925 Rensselaer Ct. Vienna, VA 22181";
		final String end = "11730 Plaza America Dr. Reston, VA 20190";
		Route route = new Route(12714, 9600);

		given(routeService.getRoute(start, end)).willReturn(route);
		Route routeTest = routeService.getRoute(start, end);
		
		assertEquals(route,routeTest);
	}
	@Test
	public void testDistance(){
		final String start = "2925 Rensselaer Ct. Vienna, VA 22181";
		final String end = "11730 Plaza America Dr. Reston, VA";
		final Route route = new Route(12714, 9600);

		given(routeService.getRoute(start, end)).willReturn(route);
		Route routeTest = routeService.getRoute(start, end);
		
		assertEquals(12714, routeTest.getDistance());
	}
	
	@Test
	public void testDuration() {
		final String start = "2925 Rensselaer Ct. Vienna, VA 22181";
		final String end = "11730 Plaza America Dr. Reston, VA";
		final Route route = new Route(12714, 9600);

		given(routeService.getRoute(start, end)).willReturn(route);
		Route routeTest = routeService.getRoute(start, end);
		
		assertEquals(routeTest.getDuration(),9600);
	}
		
		@Test
		public void testGet(){
			final String start = "2925 Rensselaer Ct. Vienna, VA 22181";
			final String end = "11730 Plaza America Dr. Reston, VA";
			final Route route = new Route(12714, 9600);

//			given(routeService1.getRoute(start, end)).willReturn(route);
//			assertEquals(routeService.getRoute(start, end), route);
//			Assert.assertEquals(routeService1.getRoute(start, end), route);
		}
	
	@Test
	public void testNegativeParams() {
	given(routeService.getRoute("-80302", "80302")).willReturn(null);
	Route negRoute= routeService.getRoute("-80302", "80302");
	assertNull(negRoute);
}
	
	@Test
	public void noEndParameters(){
		Route badRoute = routeService.getRoute("11730 Plaza America Dr. Reston, VA","");
		assertNull(badRoute);
	}
	
	@Test
	public void incompleteStartParameter() {
		Route badRoute = routeService.getRoute("11730 Plaza America Dr.","12160 Sunset Hills Rd, Reston, VA 20190");
		assertNull(badRoute);
	}
	
	@Test 
	public void incompleteEndParameter() {
		Route badRoute = routeService.getRoute("12160 Sunset Hills Rd, Reston, VA 20190","12160 Sunset Hills");
		assertNull(badRoute);
	}
	
	@Test
	public void negativeStartAddress() throws Exception {
		Route badRoute = routeService.getRoute("-12160 Sunset Hills Rd, Reston, VA 20190","12160 Sunset Hills");
		assertNull(badRoute);
	}
	
}
