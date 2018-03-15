const electron = require ('electron');
const path = require ('path');
const url = require('url');

const {app, BrowserWindow, Menu, ipcMain} = electron;

let mainWindow;

//listen for the app to be ready
app.on('ready', function(){
	//create new window
	mainWindow = new BrowserWindow({
		width: 1080,
		height: 720,
		title: 'Home Screen'
	});
	//load html
	mainWindow.loadURL(url.format({
		pathname: path.join(__dirname,'homeWindow.html'),
		protocol: 'file:',
		slashes: true
	}));
	//quit app when closing window
	mainWindow.on('closed', function(){
		app.quit();
	})

	//build template menu
	const mainMenu = Menu.buildFromTemplate(mainMenuTemplate);
	//insert menu
	Menu.setApplicationMenu(mainMenu);
});

//create custom menu
const mainMenuTemplate = [
	{
		label: 'Menu', 
		submenu: [
			{
				label: 'Quit',
				accelerator: process.platform == 'darwin' ? 'Command+Q' : 'Ctrl+Q',
				click(){
					app.quit();
				}
			}
		]
	},
	{
		label: 'View',
		submenu: [
			{
				label: 'Chart 1',
				click(){
					mainWindow.webContents.send('item:addChart');
				}
			},
			{
				label: 'Chart 2'
			},
			{
				label: 'Chart 3'
			}			
		]
	}
];


//if mac add empty object to menu
if(process.platform == 'darwin'){
	mainMenuTemplate.unshift({});
}

//add dev tools if not in production
if (process.env.NODE_ENV !== 'production'){
	mainMenuTemplate.push({
		label: 'Developer Tools',
		submenu: [
			{
				label: 'Toggle Dev Tools',
				accelerator: process.platform == 'darwin' ? 'Command+I' : 'Ctrl+I',
				click(item, focusedWindow){
					focusedWindow.toggleDevTools();
				}
			},
			{
				role: 'reload'
			}
		]
	})
}