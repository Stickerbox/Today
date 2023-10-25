# Today

An Android application that allows users to enter TODO items to quickly plan their day, as well as track the food they've eaten throughout the day.

## Technologies

- The project uses Compose for its UI, and is modularized based on feature.
- CameraX is used to take a photo of a food item.
- Realm is used to persist items in a database.

### Today View

The main view gives an overview of every item todo that day, and is themed on a randomly-generated colour, which has its complimentary colour used to create a linear gradient.

<img width="300" alt="Today view preview" src="https://github.com/Stickerbox/Today/assets/3359175/b51f90b9-b02a-496f-b5bc-1784561b0121">

### Food View

The Food view makes use of Compose's `LazyColumn` and `sectionHeader` DSL, as well as take users through a flow using CameraX to add a new food item.

